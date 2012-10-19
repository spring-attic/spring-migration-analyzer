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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.migrationanalyzer.analyze.fs.FileSystemEntry;
import org.springframework.migrationanalyzer.analyze.support.AnalysisFailedException;
import org.springframework.migrationanalyzer.analyze.support.EntryAnalyzer;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;
import org.springframework.migrationanalyzer.contributions.xml.NodeAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.StandardXmlArtifactAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.ValueAnalyzer;
import org.springframework.migrationanalyzer.contributions.xml.XmlArtifactAnalyzer;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Component
final class EjbJarXmlEntryAnalyzer implements EntryAnalyzer<Ejb> {

    @Override
    public Set<Ejb> analyze(FileSystemEntry fileSystemEntry) throws AnalysisFailedException {
        if (fileSystemEntry.getName().endsWith("ejb-jar.xml")) {
            Set<Ejb> ejbs = new HashSet<Ejb>();

            XmlArtifactAnalyzer xmlAnalyzer = new StandardXmlArtifactAnalyzer(fileSystemEntry.getInputStream(), new EjbJarEntityResolver());

            SessionBeanNodeAnalyzer sessionBeanAnalyzer = new SessionBeanNodeAnalyzer(xmlAnalyzer);
            xmlAnalyzer.analyzeNodes("/ejb-jar/enterprise-beans/session", sessionBeanAnalyzer);
            ejbs.addAll(sessionBeanAnalyzer.sessionBeans);

            EntityBeanNodeAnalyzer entityBeanAnalyzer = new EntityBeanNodeAnalyzer(xmlAnalyzer);
            xmlAnalyzer.analyzeNodes("/ejb-jar/enterprise-beans/entity", entityBeanAnalyzer);
            ejbs.addAll(entityBeanAnalyzer.entityBeans);

            MessageDrivenBeanNodeAnalyzer mdbNodeAnalyzer = new MessageDrivenBeanNodeAnalyzer(xmlAnalyzer);
            xmlAnalyzer.analyzeNodes("/ejb-jar/enterprise-beans/message-driven", mdbNodeAnalyzer);
            ejbs.addAll(mdbNodeAnalyzer.messageDriveBeans);

            ContainerTransactionNodeAnalyzer containerTransactionNodeAnalyzer = new ContainerTransactionNodeAnalyzer(xmlAnalyzer, ejbs);
            xmlAnalyzer.analyzeNodes("/ejb-jar/assembly-descriptor/container-transaction", containerTransactionNodeAnalyzer);

            return ejbs;
        }
        return Collections.<Ejb> emptySet();
    }

    private abstract static class EjbNodeAnalyzer implements NodeAnalyzer {

        private final XmlArtifactAnalyzer xmlAnalyzer;

        private EjbNodeAnalyzer(XmlArtifactAnalyzer xmlAnalyzer) {
            this.xmlAnalyzer = xmlAnalyzer;
        }

        private void analyze(Node node, Ejb ejb) throws AnalysisFailedException {
            this.xmlAnalyzer.analyzeValues(node, "ejb-name", new MethodInvokingValueAnalyzer(getSessionBeanMethod("setEjbName", String.class), ejb));
            this.xmlAnalyzer.analyzeValues(node, "ejb-class", new MethodInvokingValueAnalyzer(getSessionBeanMethod("setEjbClass", String.class), ejb));
        }

        protected XmlArtifactAnalyzer getXmlAnalyzer() {
            return this.xmlAnalyzer;
        }
    }

    private abstract static class NonMessageDrivenEjbNodeAnalyzer extends EjbNodeAnalyzer {

        private NonMessageDrivenEjbNodeAnalyzer(XmlArtifactAnalyzer xmlAnalyzer) {
            super(xmlAnalyzer);
        }

        private void analyze(Node node, NonMessageDrivenEjb ejb) throws AnalysisFailedException {
            super.analyze(node, ejb);

            getXmlAnalyzer().analyzeValues(node, "home", new MethodInvokingValueAnalyzer(getSessionBeanMethod("setHome", String.class), ejb));
            getXmlAnalyzer().analyzeValues(node, "remote", new MethodInvokingValueAnalyzer(getSessionBeanMethod("setRemote", String.class), ejb));
            getXmlAnalyzer().analyzeValues(node, "local-home",
                new MethodInvokingValueAnalyzer(getSessionBeanMethod("setLocalHome", String.class), ejb));
            getXmlAnalyzer().analyzeValues(node, "local", new MethodInvokingValueAnalyzer(getSessionBeanMethod("setLocal", String.class), ejb));
        }
    }

    private static final class EntityBeanNodeAnalyzer extends NonMessageDrivenEjbNodeAnalyzer {

        private final Set<Ejb> entityBeans = new HashSet<Ejb>();

        private EntityBeanNodeAnalyzer(XmlArtifactAnalyzer xmlAnalyzer) {
            super(xmlAnalyzer);
        }

        @Override
        public void analyze(Node node) throws AnalysisFailedException {
            EntityBean entityBean = new EntityBean();

            super.analyze(node, entityBean);

            getXmlAnalyzer().analyzeValues(node, "cmp-version",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("setCmpVersion", String.class), entityBean));
            getXmlAnalyzer().analyzeValues(node, "prim-key-class",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("setPrimaryKeyClass", String.class), entityBean));
            getXmlAnalyzer().analyzeValues(node, "persistence-type",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("setPersistenceType", String.class), entityBean));
            getXmlAnalyzer().analyzeValues(node, "reentrant",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("setReentrant", boolean.class), entityBean));
            getXmlAnalyzer().analyzeValues(node, "cmp-field/field-name",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("addCmpField", String.class), entityBean));
            getXmlAnalyzer().analyzeValues(node, "abstract-schema-name",
                new MethodInvokingValueAnalyzer(getEntityBeanMethod("setAbstractSchemaName", String.class), entityBean));

            this.entityBeans.add(entityBean);
        }

        private Method getEntityBeanMethod(String methodName, Class<?>... args) throws AnalysisFailedException {
            try {
                return EntityBean.class.getMethod(methodName, args);
            } catch (NoSuchMethodException nsme) {
                throw new AnalysisFailedException("Failed to get method '" + methodName + "' on EntityBean", nsme);
            }
        }
    }

    private static final class MessageDrivenBeanNodeAnalyzer extends EjbNodeAnalyzer {

        private final Set<Ejb> messageDriveBeans = new HashSet<Ejb>();

        private MessageDrivenBeanNodeAnalyzer(XmlArtifactAnalyzer analyzer) {
            super(analyzer);
        }

        @Override
        public void analyze(Node node) throws AnalysisFailedException {
            MessageDrivenBean messageDrivenBean = new MessageDrivenBean();

            super.analyze(node, messageDrivenBean);

            getXmlAnalyzer().analyzeValues(node, "transaction-type",
                new MethodInvokingValueAnalyzer(getMessageDrivenBeanMethod("setTransactionType", String.class), messageDrivenBean));

            this.messageDriveBeans.add(messageDrivenBean);
        }

        private Method getMessageDrivenBeanMethod(String methodName, Class<?>... args) throws AnalysisFailedException {
            try {
                return MessageDrivenBean.class.getMethod(methodName, args);
            } catch (NoSuchMethodException nsme) {
                throw new AnalysisFailedException("Failed to get method '" + methodName + "' on MessageDrivenBean", nsme);
            }
        }
    }

    private static final class SessionBeanNodeAnalyzer extends NonMessageDrivenEjbNodeAnalyzer {

        private final Set<Ejb> sessionBeans = new HashSet<Ejb>();

        private SessionBeanNodeAnalyzer(XmlArtifactAnalyzer analyzer) {
            super(analyzer);
        }

        @Override
        public void analyze(Node node) throws AnalysisFailedException {
            SessionBean sessionBean = new SessionBean();

            super.analyze(node, sessionBean);

            getXmlAnalyzer().analyzeValues(node, "business-local",
                new MethodInvokingValueAnalyzer(getSessionBeanMethod("setBusinessLocal", String.class), sessionBean));
            getXmlAnalyzer().analyzeValues(node, "business-remote",
                new MethodInvokingValueAnalyzer(getSessionBeanMethod("setBusinessRemote", String.class), sessionBean));

            getXmlAnalyzer().analyzeValues(node, "service-endpoint",
                new MethodInvokingValueAnalyzer(getSessionBeanMethod("setServiceEndpoint", String.class), sessionBean));
            getXmlAnalyzer().analyzeValues(node, "session-type", new SessionTypeValueAnalyzer(sessionBean));
            getXmlAnalyzer().analyzeValues(node, "transaction-type",
                new MethodInvokingValueAnalyzer(getSessionBeanMethod("setTransactionType", String.class), sessionBean));

            this.sessionBeans.add(sessionBean);
        }
    }

    private static Method getSessionBeanMethod(String methodName, Class<?>... args) throws AnalysisFailedException {
        try {
            return SessionBean.class.getMethod(methodName, args);
        } catch (NoSuchMethodException nsme) {
            throw new AnalysisFailedException("Failed to get method '" + methodName + "' on SessionBean", nsme);
        }
    }

    private static final class MethodInvokingValueAnalyzer implements ValueAnalyzer {

        private final Method method;

        private final Object target;

        private MethodInvokingValueAnalyzer(Method method, Object target) {
            this.method = method;
            this.target = target;
        }

        @Override
        public void analyse(String value) throws AnalysisFailedException {
            Class<?>[] parameterTypes = this.method.getParameterTypes();
            Class<?> parameterType = parameterTypes[0];

            try {
                if (boolean.class.equals(parameterType)) {
                    this.method.invoke(this.target, Boolean.valueOf(value.toLowerCase(Locale.ENGLISH)));
                } else {
                    this.method.invoke(this.target, value);
                }
            } catch (InvocationTargetException e) {
                throw new AnalysisFailedException(String.format("Failed to invoke method '%s' on target '%s' with argument '%s'", this.method,
                    this.target, value), e);
            } catch (IllegalArgumentException e) {
                throw new AnalysisFailedException(String.format("Failed to invoke method '%s' on target '%s' with argument '%s'", this.method,
                    this.target, value), e);
            } catch (IllegalAccessException e) {
                throw new AnalysisFailedException(String.format("Failed to invoke method '%s' on target '%s' with argument '%s'", this.method,
                    this.target, value), e);
            }
        }
    }

    private static final class SessionTypeValueAnalyzer implements ValueAnalyzer {

        private static final String SESSION_TYPE_STATELESS = "Stateless";

        private static final String SESSION_TYPE_STATEFUL = "Stateful";

        private final SessionBean sessionBean;

        private SessionTypeValueAnalyzer(SessionBean sessionBean) {
            this.sessionBean = sessionBean;
        }

        @Override
        public void analyse(String value) {
            if (SESSION_TYPE_STATEFUL.equals(value)) {
                this.sessionBean.setSessionType(SessionType.STATEFUL);
            } else if (SESSION_TYPE_STATELESS.equals(value)) {
                this.sessionBean.setSessionType(SessionType.STATELESS);
            }
        }
    }

    private static final class ContainerTransactionNodeAnalyzer implements NodeAnalyzer {

        private final XmlArtifactAnalyzer xmlAnalyzer;

        private final Set<Ejb> ejbs;

        private ContainerTransactionNodeAnalyzer(XmlArtifactAnalyzer xmlAnalyzer, Set<Ejb> ejbs) {
            this.xmlAnalyzer = xmlAnalyzer;
            this.ejbs = ejbs;
        }

        @Override
        public void analyze(Node node) throws AnalysisFailedException {
            ValueCollectingValueAnalyzer transAttributeAnalyzer = new ValueCollectingValueAnalyzer();
            this.xmlAnalyzer.analyzeValues(node, "trans-attribute", transAttributeAnalyzer);

            ValueCollectingValueAnalyzer ejbNameAnalyzer = new ValueCollectingValueAnalyzer();
            this.xmlAnalyzer.analyzeValues(node, "method/ejb-name", ejbNameAnalyzer);

            List<String> ejbNames = ejbNameAnalyzer.getValues();

            Set<TransactionPropagationType> propagationTypes = getPropagationTypes(transAttributeAnalyzer);

            for (String ejbName : ejbNames) {
                for (Ejb ejb : this.ejbs) {
                    if (ejbName.equals(ejb.getEjbName()) && (ejb instanceof TransactionalEjb)) {
                        ((TransactionalEjb) ejb).addTransactionPropagationTypes(propagationTypes);
                    }
                }
            }
        }

        private Set<TransactionPropagationType> getPropagationTypes(ValueCollectingValueAnalyzer transAttributeAnalyzer) {
            Set<TransactionPropagationType> propagationTypes = new HashSet<TransactionPropagationType>();
            for (String transAttribute : transAttributeAnalyzer.getValues()) {
                if ("Required".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.REQUIRED);
                } else if ("RequiresNew".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.REQUIRES_NEW);
                } else if ("NotSupported".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.NOT_SUPPORTED);
                } else if ("Supports".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.SUPPORTS);
                } else if ("Never".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.NEVER);
                } else if ("Mandatory".equals(transAttribute)) {
                    propagationTypes.add(TransactionPropagationType.MANDATORY);
                }
            }
            return propagationTypes;
        }
    }

    private static final class ValueCollectingValueAnalyzer implements ValueAnalyzer {

        private final List<String> values = new ArrayList<String>();

        @Override
        public void analyse(String value) {
            this.values.add(value);
        }

        private List<String> getValues() {
            return this.values;
        }
    }

    private static final class EjbJarEntityResolver implements EntityResolver {

        private static final String SYSTEM_ID_EJB_JAR_2_0_DTD = "http://java.sun.com/dtd/ejb-jar_2_0.dtd";

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (SYSTEM_ID_EJB_JAR_2_0_DTD.equals(systemId)) {
                return new InputSource(getClass().getResourceAsStream("ejb-jar_2_0.dtd"));
            }
            return null;
        }

    }
}
