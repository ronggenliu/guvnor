package org.drools.guvnor.client.explorer.navigation;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.drools.guvnor.client.common.AssetFormats;
import org.drools.guvnor.client.explorer.navigation.ModulesNewAssetMenuView.Presenter;
import org.drools.guvnor.client.rpc.PackageServiceAsync;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ModulesNewAssetMenuTest {

    private ModulesNewAssetMenuView view;
    private Presenter presenter;
    private PackageServiceAsync packageService;

    @Before
    public void setUp() throws Exception {
        view = mock( ModulesNewAssetMenuView.class );
        packageService = mock( PackageServiceAsync.class );
        presenter = new ModulesNewAssetMenu( view, packageService );
    }

    @Test
    public void testIsPresenterSet() throws Exception {
        verify( view ).setPresenter( presenter );
    }

    @Test
    public void testNewModule() throws Exception {
        presenter.onNewModule();
        verify( view ).openNewPackageWizard();
    }

    @Test
    public void testNewSpringContext() throws Exception {
        presenter.onNewSpringContext();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.SPRING_CONTEXT );
    }

    @Test
    public void testNewWorkingSet() throws Exception {
        presenter.onNewWorkingSet();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.WORKING_SET );
    }

    @Test
    public void testNewRule() throws Exception {
        presenter.onNewRule();
        verify( view ).openNewAssetWizardWithCategories( null );
    }

    @Test
    public void testNewRuleTemplate() throws Exception {
        presenter.onNewRuleTemplate();
        verify( view ).openNewAssetWizardWithCategories( AssetFormats.RULE_TEMPLATE );
    }

    @Test
    public void testNewPojoModel() throws Exception {
        presenter.onNewPojoModel();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.MODEL );
    }

    @Test
    public void testNewDeclarativeModel() throws Exception {
        presenter.onNewDeclarativeModel();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.DRL_MODEL );
    }

    @Test
    public void testNewBPELPackage() throws Exception {
        presenter.onNewBPELPackage();
        verify( view ).openNewAssetWizardWithCategories( AssetFormats.BPEL_PACKAGE );
    }

    @Test
    public void testNewFunction() throws Exception {
        presenter.onNewFunction();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.FUNCTION );
    }

    @Test
    public void testNewDSL() throws Exception {
        presenter.onNewDSL();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.DSL );
    }

    @Test
    public void testNewRuleFlow() throws Exception {
        presenter.onNewRuleFlow();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.RULE_FLOW_RF );
    }

    @Test
    public void testNewBPMN2Process() throws Exception {
        presenter.onNewBPMN2Process();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.BPMN2_PROCESS );
    }

    @Test
    public void testNewWorkitemDefinition() throws Exception {
        presenter.onNewWorkitemDefinition();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.WORKITEM_DEFINITION );
    }

    @Test
    public void testNewEnumeration() throws Exception {
        presenter.onNewEnumeration();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.ENUMERATION );
    }

    @Test
    public void testNewTestScenario() throws Exception {
        presenter.onNewTestScenario();
        verify( view ).openNewAssetWizardWithoutCategories( AssetFormats.TEST_SCENARIO );
    }

    @Test
    public void testNewFile() throws Exception {
        presenter.onNewFile();
        verify( view ).openNewAssetWizardWithoutCategories( "*" );
    }

    @Test
    public void testRebuildAllPackages() throws Exception {
        presenter.onRebuildAllPackages();
        verify( view ).confirmRebuild();
        presenter.onRebuildConfirmed();
        verify( view ).showLoadingPopUpRebuildingPackageBinaries();

        ArgumentCaptor<AsyncCallback> argumentCaptor = ArgumentCaptor.forClass( AsyncCallback.class );

        verify( packageService ).rebuildPackages( argumentCaptor.capture() );

        argumentCaptor.getValue().onSuccess( null );

        verify( view ).closeLoadingPopUp();
    }
}
