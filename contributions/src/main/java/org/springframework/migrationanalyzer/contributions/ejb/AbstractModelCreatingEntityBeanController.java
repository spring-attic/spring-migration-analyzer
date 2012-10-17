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
import org.springframework.migrationanalyzer.render.ModelAndView;

abstract class AbstractModelCreatingEntityBeanController extends AbstractEntityBeanController {

    private final String viewName;

    protected AbstractModelCreatingEntityBeanController(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public ModelAndView handle(Set<AnalysisResultEntry<EntityBean>> results) {
        return new ModelAndView(createModel(results), this.viewName);
    }

    protected Map<String, Object> createModel(Set<AnalysisResultEntry<EntityBean>> results) {
        Map<String, Map<String, String>> entityBeans = new TreeMap<String, Map<String, String>>();

        for (AnalysisResultEntry<EntityBean> analysisResultEntry : results) {
            EntityBean entityBean = analysisResultEntry.getResult();

            Map<String, String> metadata = new TreeMap<String, String>();
            addEjbClass(entityBean, metadata);
            addHome(entityBean, metadata);
            addRemote(entityBean, metadata);
            addLocalHome(entityBean, metadata);
            addLocal(entityBean, metadata);
            addPersistenceType(entityBean, metadata);
            addPrimaryKeyClass(entityBean, metadata);
            addReentrant(entityBean, metadata);
            addCmpVersion(entityBean, metadata);
            addCmpFields(entityBean, metadata);
            addAbstractSchemaName(entityBean, metadata);

            entityBeans.put(entityBean.getEjbName(), metadata);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", getTitle(results.size()));
        model.put("entityBeans", entityBeans);

        return model;
    }

    private String getTitle(int size) {
        if (size == 1) {
            return "1 Entity Bean";
        }
        return String.format("%s Entity Beans", size);
    }

    private void addEjbClass(EntityBean entityBean, Map<String, String> metadata) {
        String ejbClass = entityBean.getEjbClass();
        if (ejbClass != null) {
            metadata.put("EJB Class", ejbClass);
        }
    }

    private void addHome(EntityBean entityBean, Map<String, String> metadata) {
        String home = entityBean.getHome();
        if (home != null) {
            metadata.put("Home", home);
        }
    }

    private void addRemote(EntityBean entityBean, Map<String, String> metadata) {
        String remote = entityBean.getRemote();
        if (remote != null) {
            metadata.put("Remote", remote);
        }
    }

    private void addLocalHome(EntityBean entityBean, Map<String, String> metadata) {
        String localHome = entityBean.getLocalHome();
        if (localHome != null) {
            metadata.put("Local Home", localHome);
        }
    }

    private void addLocal(EntityBean entityBean, Map<String, String> metadata) {
        String local = entityBean.getLocal();
        if (local != null) {
            metadata.put("Local", local);
        }
    }

    private void addPersistenceType(EntityBean entityBean, Map<String, String> metadata) {
        String persistenceType = entityBean.getPersistenceType();
        if (persistenceType != null) {
            metadata.put("Persistence Type", persistenceType);
        }
    }

    private void addPrimaryKeyClass(EntityBean entityBean, Map<String, String> metadata) {
        String primaryKeyClass = entityBean.getPrimaryKeyClass();
        if (primaryKeyClass != null) {
            metadata.put("Primary Key Class", primaryKeyClass);
        }
    }

    private void addReentrant(EntityBean entityBean, Map<String, String> metadata) {
        metadata.put("Reentrant", String.valueOf(entityBean.isReentrant()));
    }

    private void addCmpVersion(EntityBean entityBean, Map<String, String> metadata) {
        String cmpVersion = entityBean.getCmpVersion();
        if (cmpVersion != null) {
            metadata.put("CMP Version", cmpVersion);
        }
    }

    private void addCmpFields(EntityBean entityBean, Map<String, String> metadata) {
        Set<String> cmpFields = entityBean.getCmpFields();
        if (!cmpFields.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String cmpField : cmpFields) {
                sb.append(" ").append(cmpField);
            }
            sb.deleteCharAt(0);
            metadata.put("CMP Fields", sb.toString());
        }
    }

    private void addAbstractSchemaName(EntityBean entityBean, Map<String, String> metadata) {
        String abstractSchemaName = entityBean.getAbstractSchemaName();
        if (abstractSchemaName != null) {
            metadata.put("Abstract Schema Name", abstractSchemaName);
        }
    }
}
