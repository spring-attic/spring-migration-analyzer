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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.migrationanalyzer.analyze.AnalysisResultEntry;
import org.springframework.migrationanalyzer.contributions.transactions.TransactionPropagationType;
import org.springframework.migrationanalyzer.contributions.util.StringUtils;
import org.springframework.migrationanalyzer.render.ModelAndView;
import org.springframework.migrationanalyzer.render.OutputPathGenerator;

abstract class AbstractModelCreatingSessionBeanController extends AbstractSessionBeanController {

    private final String viewName;

    protected AbstractModelCreatingSessionBeanController(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<SessionBean>> results, OutputPathGenerator outputPathGenerator) {
        return new ModelAndView(createModel(results), this.viewName);
    }

    protected Map<String, Object> createModel(Set<AnalysisResultEntry<SessionBean>> results) {
        Map<String, Map<String, String>> statefulSessionBeans = new TreeMap<String, Map<String, String>>();
        Map<String, Map<String, String>> statelessSessionBeans = new TreeMap<String, Map<String, String>>();

        for (AnalysisResultEntry<SessionBean> analysisResultEntry : results) {
            SessionBean sessionBean = analysisResultEntry.getResult();

            Map<String, String> metadata = new TreeMap<String, String>();
            addEjbClass(sessionBean, metadata);
            addBusinessLocal(sessionBean, metadata);
            addBusinessRemote(sessionBean, metadata);
            addHome(sessionBean, metadata);
            addRemote(sessionBean, metadata);
            addLocalHome(sessionBean, metadata);
            addLocal(sessionBean, metadata);
            addServiceEndpoint(sessionBean, metadata);
            addTransactionType(sessionBean, metadata);

            if ("Container".equals(sessionBean.getTransactionType())) {
                addTransactionPropagationTypes(sessionBean, metadata);
            }

            if (sessionBean.getSessionType() == SessionType.STATEFUL) {
                statefulSessionBeans.put(sessionBean.getEjbName(), metadata);
            } else {
                statelessSessionBeans.put(sessionBean.getEjbName(), metadata);
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("sessionBeansTitle", getTitle(results.size()));
        model.put("statefulSessionBeansTitle", getStatefulTitle(statefulSessionBeans.size()));
        model.put("statelessSessionBeansTitle", getStatelessTitle(statelessSessionBeans.size()));
        model.put("statefulSessionBeans", statefulSessionBeans);
        model.put("statelessSessionBeans", statelessSessionBeans);

        return model;
    }

    private String getTitle(int size) {
        if (size == 1) {
            return "1 Session Bean";
        }
        return String.format("%s Session Beans", size);
    }

    private String getStatefulTitle(int size) {
        if (size == 1) {
            return "1 Stateful Session Bean";
        }
        return String.format("%s Stateful Session Beans", size);
    }

    private String getStatelessTitle(int size) {
        if (size == 1) {
            return "1 Stateless Session Bean";
        }
        return String.format("%s Stateless Session Beans", size);
    }

    private void addEjbClass(SessionBean sessionBean, Map<String, String> metadata) {
        String ejbClass = sessionBean.getEjbClass();
        if (ejbClass != null) {
            metadata.put("EJB Class", ejbClass);
        }
    }

    private void addBusinessLocal(SessionBean sessionBean, Map<String, String> metadata) {
        String businessLocal = sessionBean.getBusinessLocal();
        if (businessLocal != null) {
            metadata.put("Business Local", businessLocal);
        }
    }

    private void addBusinessRemote(SessionBean sessionBean, Map<String, String> metadata) {
        String businessRemote = sessionBean.getBusinessRemote();
        if (businessRemote != null) {
            metadata.put("Business Remote", businessRemote);
        }
    }

    private void addHome(SessionBean sessionBean, Map<String, String> metadata) {
        String home = sessionBean.getHome();
        if (home != null) {
            metadata.put("Home", home);
        }
    }

    private void addRemote(SessionBean sessionBean, Map<String, String> metadata) {
        String remote = sessionBean.getRemote();
        if (remote != null) {
            metadata.put("Remote", remote);
        }
    }

    private void addLocalHome(SessionBean sessionBean, Map<String, String> metadata) {
        String localHome = sessionBean.getLocalHome();
        if (localHome != null) {
            metadata.put("Local Home", localHome);
        }
    }

    private void addLocal(SessionBean sessionBean, Map<String, String> metadata) {
        String local = sessionBean.getLocal();
        if (local != null) {
            metadata.put("Local", local);
        }
    }

    private void addServiceEndpoint(SessionBean sessionBean, Map<String, String> metadata) {
        String serviceEndpoint = sessionBean.getServiceEndpoint();
        if (serviceEndpoint != null) {
            metadata.put("Service Endpoint", serviceEndpoint);
        }
    }

    private void addTransactionType(SessionBean sessionBean, Map<String, String> metadata) {
        metadata.put("Transaction Type", sessionBean.getTransactionType());
    }

    private void addTransactionPropagationTypes(SessionBean sessionBean, Map<String, String> metadata) {
        Set<TransactionPropagationType> transactionPropagationTypes = sessionBean.getTransactionPropagationTypes();
        if (!transactionPropagationTypes.isEmpty()) {
            metadata.put("Declarative transactions", StringUtils.toSeparatedString(transactionPropagationTypes, ", "));
        }
    }
}
