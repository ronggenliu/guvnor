/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.guvnor.client.explorer.navigation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.resources.Images;

public class KnowledgeModulesTreeViewImpl extends Composite implements KnowledgeModulesTreeView {

    interface KnowledgeModulesTreeViewImplBinder
            extends
            UiBinder<Widget, KnowledgeModulesTreeViewImpl> {
    }

    private static KnowledgeModulesTreeViewImplBinder uiBinder = GWT.create( KnowledgeModulesTreeViewImplBinder.class );

    @UiField
    SimplePanel menuContainer;

    @UiField
    SimplePanel modulesTreeContainer;

    @UiField
    SimplePanel globalModulesTreeContainer;

    public KnowledgeModulesTreeViewImpl() {
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    public void setNewAssetMenu( ModulesNewAssetMenu modulesNewAssetMenu ) {
        menuContainer.setWidget( modulesNewAssetMenu );
    }

    public void setGlobalAreaTreeItem( GlobalAreaTreeItem globalAreaTreeItem ) {
        globalModulesTreeContainer.setWidget( globalAreaTreeItem.asWidget() );
    }

    public void setKnowledgeModulesTreeItem( KnowledgeModulesTreeItem knowledgeModulesTreeItem ) {
        modulesTreeContainer.setWidget( knowledgeModulesTreeItem );
    }
}
