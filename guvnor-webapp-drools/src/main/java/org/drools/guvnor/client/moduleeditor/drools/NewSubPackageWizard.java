/*
 * Copyright 2005 JBoss Inc
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

package org.drools.guvnor.client.moduleeditor.drools;

import com.google.gwt.user.client.ui.*;
import org.drools.guvnor.client.common.ErrorPopup;
import org.drools.guvnor.client.common.FormStyleLayout;
import org.drools.guvnor.client.common.FormStylePopup;
import org.drools.guvnor.client.common.GenericCallback;
import org.drools.guvnor.client.common.HTMLFileManagerFields;
import org.drools.guvnor.client.common.ImageButton;
import org.drools.guvnor.client.common.LoadingPopup;
import org.drools.guvnor.client.common.RulePackageSelector;
import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.moduleeditor.ModuleNameValidator;
import org.drools.guvnor.client.resources.DroolsGuvnorImageResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import org.drools.guvnor.client.resources.DroolsGuvnorImages;
import org.drools.guvnor.client.rpc.ModuleService;
import org.drools.guvnor.client.rpc.ModuleServiceAsync;

/**
 * This is the wizard used when creating new packages or importing them.
 */
public class NewSubPackageWizard extends FormStylePopup {

    private TextBox               nameBox;
    private TextBox               descBox;
    private RulePackageSelector   parentPackage;
    private final FormStyleLayout importLayout     = new FormStyleLayout();
    private final FormStyleLayout newPackageLayout = new FormStyleLayout();

    public NewSubPackageWizard(final Command afterCreatedEvent) {
        super(DroolsGuvnorImages.INSTANCE.Wizard(),
               Constants.INSTANCE.CreateANewSubPackage() );
        nameBox = new TextBox();
        descBox = new TextBox();
        parentPackage = new RulePackageSelector();

        newPackageLayout.addAttribute( Constants.INSTANCE.NameColon(),
                                       nameBox );
        newPackageLayout.addAttribute( Constants.INSTANCE.DescriptionColon(),
                                       descBox );
        newPackageLayout.addAttribute( Constants.INSTANCE.ParentPackage(),
                                       parentPackage );

        nameBox.setTitle( Constants.INSTANCE.PackageNameTip() );

        RadioButton newPackage = new RadioButton( "action",
                                                  Constants.INSTANCE.CreateNewPackageRadio() ); //NON-NLS
        RadioButton importPackage = new RadioButton( "action",
                                                     Constants.INSTANCE.ImportFromDrlRadio() ); //NON-NLS
        newPackage.setValue( true );
        newPackageLayout.setVisible( true );

        newPackage.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                newPackageLayout.setVisible( true );
                importLayout.setVisible( false );
            }
        } );

        this.setAfterShow( new Command() {
            public void execute() {
                nameBox.setFocus( true );
            }
        } );

        importLayout.setVisible( false );

        importPackage.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                newPackageLayout.setVisible( false );
                importLayout.setVisible( true );
            }
        } );
        VerticalPanel ab = new VerticalPanel();
        ab.add( newPackage );
        ab.add( importPackage );
        addAttribute( "",
                      ab );

        addRow( newPackageLayout );
        addRow( importLayout );

        importLayout.addAttribute( Constants.INSTANCE.DRLFileToImport(),
                                   newImportWidget( afterCreatedEvent,
                                                    this ) );

        importLayout.addRow( new HTML( "<br/><b>" + Constants.INSTANCE.NoteNewPackageDrlImportWarning() + "</b>" ) );
        importLayout.addRow( new HTML( Constants.INSTANCE.ImportDRLDesc1() ) );
        importLayout.addRow( new HTML( Constants.INSTANCE.ImportDRLDesc2() ) );
        importLayout.addRow( new HTML( Constants.INSTANCE.ImportDRLDesc3() ) );

        Button create = new Button( Constants.INSTANCE.CreatePackage() );
        create.addClickHandler( new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ( ModuleNameValidator.validatePackageName( nameBox.getText() ) ) {
                    createSubPackageAction( nameBox.getText(),
                                            descBox.getText(),
                                            parentPackage.getSelectedPackage(),
                                            afterCreatedEvent );
                    hide();
                } else {
                    nameBox.setText( "" );
                    Window.alert( Constants.INSTANCE.PackageNameCorrectHint() );
                }
            }
        } );

        newPackageLayout.addAttribute( "",
                                       create );

    }

    private void createSubPackageAction(final String name,
                                        final String descr,
                                        String parentPackage,
                                        final Command refresh) {
        LoadingPopup.showMessage( Constants.INSTANCE.CreatingPackagePleaseWait() );
        ModuleServiceAsync moduleService = GWT.create(ModuleService.class);
        moduleService.createSubModule(name,
                                    descr,
                                    parentPackage,
                                    new GenericCallback<String>() {
                                        public void onSuccess(String data) {
                                            RulePackageSelector.currentlySelectedPackage = name;
                                            LoadingPopup.close();
                                            refresh.execute();
                                        }
                                    });
    }

    public static Widget newImportWidget(final Command afterCreatedEvent,
                                         final FormStylePopup parent) {

        final FormPanel uploadFormPanel = new FormPanel();
        uploadFormPanel.setAction( GWT.getModuleBaseURL() + "package" );
        uploadFormPanel.setEncoding( FormPanel.ENCODING_MULTIPART );
        uploadFormPanel.setMethod( FormPanel.METHOD_POST );

        HorizontalPanel panel = new HorizontalPanel();
        uploadFormPanel.setWidget( panel );

        final FileUpload upload = new FileUpload();
        upload.setName( HTMLFileManagerFields.CLASSIC_DRL_IMPORT );
        panel.add( upload );

        panel.add( new Label( Constants.INSTANCE.upload() ) );
        ImageButton ok = new ImageButton( DroolsGuvnorImageResources.INSTANCE.upload(),
                                          Constants.INSTANCE.Import() );
        ClickHandler okClickHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                if ( Window.confirm( Constants.INSTANCE.ImportMergeWarning() ) ) {
                    LoadingPopup.showMessage( Constants.INSTANCE.ImportingDRLPleaseWait() );
                    uploadFormPanel.submit();
                }
            }

        };
        ok.addClickHandler( okClickHandler );

        panel.add( ok );

        Image image = new Image(DroolsGuvnorImageResources.INSTANCE.packageLarge());
        image.setAltText(Constants.INSTANCE.Package());
        final FormStylePopup packageNamePopup = new FormStylePopup(image,
                                                                    Constants.INSTANCE.PackageName() );
        HorizontalPanel packageNamePanel = new HorizontalPanel();
        packageNamePopup.addRow( new Label( Constants.INSTANCE.ImportedDRLContainsNoNameForThePackage() ) );

        final TextBox packageName = new TextBox();
        packageNamePanel.add( new Label( Constants.INSTANCE.PackageName() + ":" ) );
        packageNamePanel.add( packageName );
        Button uploadWithNameButton = new Button( Constants.INSTANCE.OK() );
        uploadWithNameButton.addClickHandler( okClickHandler );
        packageNamePanel.add( uploadWithNameButton );
        packageNamePopup.addRow( packageNamePanel );

        uploadFormPanel.addSubmitCompleteHandler( new SubmitCompleteHandler() {
            public void onSubmitComplete(SubmitCompleteEvent event) {
                if ( event.getResults().indexOf( "OK" ) > -1 ) { //NON-NLS
                    LoadingPopup.close();
                    Window.alert( Constants.INSTANCE.PackageWasImportedSuccessfully() );
                    afterCreatedEvent.execute();
                    parent.hide();
                    if ( packageNamePopup != null ) {
                        packageNamePopup.hide();
                    }
                } else if ( event.getResults().indexOf( "Missing package name." ) > -1 ) { //NON-NLS
                    LoadingPopup.close();
                    packageNamePopup.show();
                } else {
                    ErrorPopup.showMessage( Constants.INSTANCE.UnableToImportIntoThePackage0( event.getResults() ) );
                }
                LoadingPopup.close();
            }
        } );
        uploadFormPanel.addSubmitHandler( new SubmitHandler() {
            public void onSubmit(SubmitEvent event) {
                if ( upload.getFilename().length() == 0 ) {
                    Window.alert( Constants.INSTANCE.YouDidNotChooseADrlFileToImport() );
                    event.cancel();
                } else if ( !upload.getFilename().endsWith( ".drl" ) ) { //NON-NLS
                    Window.alert( Constants.INSTANCE.YouCanOnlyImportDrlFiles() );
                    event.cancel();
                } else if ( packageName.getText() != null && !packageName.getText().equals( "" ) ) {
                    uploadFormPanel.setAction( uploadFormPanel.getAction() + "?packageName=" + packageName.getText() );
                } else {
                    LoadingPopup.showMessage( Constants.INSTANCE.CreatingPackagePleaseWait() );
                }
            }
        } );

        return uploadFormPanel;
    }

}
