/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.migrationanalyzer.contributions.ejb;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.springframework.migrationanalyzer.contributions.bytecode.ResultGatheringClassVisitor;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;

final class EjbDetectingClassVisitor extends EmptyVisitor implements ResultGatheringClassVisitor<Ejb> {

    private static final String CLASS_NAME_STATELESS_ANNOTATION = "javax.ejb.Stateless";

    private static final String CLASS_NAME_STATEFUL_ANNOTATION = "javax.ejb.Stateful";

    private static final String CLASS_NAME_MESSAGE_DRIVEN = "javax.ejb.MessageDriven";

    private static final String CLASS_NAME_TRANSACTION_MANAGEMENT = "javax.ejb.TransactionManagement";

    private static final String CLASS_NAME_TRANSACTION_ATTRIBUTE = "javax.ejb.TransactionAttribute";

    private Ejb currentEjb = null;

    private String currentTransactionManagementType;

    private final Set<TransactionPropagationType> currentPropagationTypes = new HashSet<TransactionPropagationType>();

    private String className;

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = Type.getObjectType(name).getClassName();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        String annotationClassName = Type.getType(desc).getClassName();

        if (CLASS_NAME_STATELESS_ANNOTATION.equals(annotationClassName)) {
            SessionBean sessionBean = getSessionBean();
            sessionBean.setEjbClass(this.className);
            sessionBean.setSessionType(SessionType.STATELESS);
            return new NameExtractingAnnotationVisitor(sessionBean);
        } else if (CLASS_NAME_STATEFUL_ANNOTATION.equals(annotationClassName)) {
            SessionBean sessionBean = getSessionBean();
            sessionBean.setEjbClass(this.className);
            sessionBean.setSessionType(SessionType.STATEFUL);
            return new NameExtractingAnnotationVisitor(sessionBean);
        } else if (CLASS_NAME_MESSAGE_DRIVEN.equals(annotationClassName)) {
            MessageDrivenBean messageDrivenBean = getMessageDrivenBean();
            messageDrivenBean.setEjbClass(this.className);
            return new NameExtractingAnnotationVisitor(messageDrivenBean);
        } else if (CLASS_NAME_TRANSACTION_MANAGEMENT.equals(annotationClassName)) {
            return new TransactionManagementAnnotationVisitor();
        } else if (CLASS_NAME_TRANSACTION_ATTRIBUTE.equals(annotationClassName)) {
            return new TransactionAttributeAnnotationVisitor();
        }

        return null;
    }

    @Override
    public Set<Ejb> getResults() {
        Set<Ejb> ejbs = new HashSet<Ejb>();

        if (this.currentEjb != null) {
            if (this.currentEjb instanceof TransactionalEjb) {
                if ("Bean".equals(this.currentTransactionManagementType)) {
                    ((TransactionalEjb) this.currentEjb).setTransactionType("Bean");
                } else {
                    ((TransactionalEjb) this.currentEjb).addTransactionPropagationTypes(this.currentPropagationTypes);
                }
            }

            ejbs.add(this.currentEjb);
        }

        this.currentEjb = null;
        this.currentTransactionManagementType = null;
        this.currentPropagationTypes.clear();

        return ejbs;
    }

    private SessionBean getSessionBean() {
        if (this.currentEjb == null) {
            this.currentEjb = new SessionBean();
            return (SessionBean) this.currentEjb;
        } else {
            throw new IllegalStateException("An EJB has already been detected");
        }
    }

    private MessageDrivenBean getMessageDrivenBean() {
        if (this.currentEjb == null) {
            this.currentEjb = new MessageDrivenBean();
            return (MessageDrivenBean) this.currentEjb;
        } else {
            throw new IllegalStateException("An EJB has already been detected");
        }
    }

    private final class TransactionManagementAnnotationVisitor extends EmptyVisitor {

        @Override
        public void visitEnum(String name, String desc, String value) {
            if ("BEAN".equals(value)) {
                EjbDetectingClassVisitor.this.currentTransactionManagementType = "Bean";
            }
        }
    }

    private static final class NameExtractingAnnotationVisitor extends EmptyVisitor {

        private final Ejb ejb;

        NameExtractingAnnotationVisitor(Ejb ejb) {
            this.ejb = ejb;
        }

        @Override
        public void visit(String name, Object value) {
            if ("name".equals(name)) {
                this.ejb.setEjbName(value.toString());
            }
        }
    }

    private final class TransactionAttributeAnnotationVisitor extends EmptyVisitor {

        @Override
        public void visitEnum(String name, String desc, String value) {
            if ("value".equals(name)) {
                TransactionPropagationType propagationType = TransactionPropagationType.valueOf(value);
                EjbDetectingClassVisitor.this.currentPropagationTypes.add(propagationType);
            }
        }
    }
}
