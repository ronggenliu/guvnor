package org.drools.guvnor.client.explorer.navigation;

import org.drools.guvnor.client.configurations.Capability;
import org.drools.guvnor.client.configurations.UserCapabilities;
import org.drools.guvnor.client.explorer.ClientFactory;
import org.drools.guvnor.client.rpc.PackageServiceAsync;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static org.mockito.Mockito.*;

public class KnowledgesModuleTreeTest {

    private KnowledgeModulesTreeView view;
    private KnowledgesModuleTree presenter;

    @Before
    public void setUp() throws Exception {
        view = mock( KnowledgeModulesTreeView.class );
    }


    @Test
    public void testNewAssetMenuIsSet() throws Exception {
        setUpPresenter();

        verify( view ).setNewAssetMenu( any( ModulesNewAssetMenu.class ) );
    }

    @Test
    public void testNewAssetMenuIsNotSet() throws Exception {
        setUpUserCapabilities( false );

        createPresenter();

        verify( view, never() ).setNewAssetMenu( any( ModulesNewAssetMenu.class ) );
    }

    @Test
    public void testRootItemsAreSet() throws Exception {
        setUpPresenter();

        verify( view ).setGlobalAreaTreeItem( Matchers.<GlobalAreaTreeItem>any() );
        verify( view ).setKnowledgeModulesTreeItem( Matchers.<KnowledgeModulesTreeItem>any() );
    }

    private void setUpPresenter() {
        setUpUserCapabilities( true );
        createPresenter();
    }

    private void createPresenter() {
        ClientFactory clientFactory = mock( ClientFactory.class );

        NavigationViewFactory navigationViewFactory = mock( NavigationViewFactory.class );
        when(
                clientFactory.getNavigationViewFactory()
        ).thenReturn(
                navigationViewFactory
        );

        when(
                navigationViewFactory.getKnowledgeModulesTreeView()
        ).thenReturn(
                view
        );

        KnowledgeModulesTreeItemView knowledgeModulesTreeItemView = mock( KnowledgeModulesTreeItemView.class );
        when(
                navigationViewFactory.getKnowledgeModulesTreeItemView()
        ).thenReturn(
                knowledgeModulesTreeItemView
        );

        PackageServiceAsync packageService = mock( PackageServiceAsync.class );
        when(
                clientFactory.getPackageService()
        ).thenReturn(
                packageService
        );

        ModulesNewAssetMenuView modulesNewAssetMenuView = mock( ModulesNewAssetMenuView.class );
        when(
                navigationViewFactory.getModulesNewAssetMenuView()
        ).thenReturn(
                modulesNewAssetMenuView
        );


        presenter = new KnowledgesModuleTree( clientFactory );
    }

    private void setUpUserCapabilities( boolean canMakeNewAssets ) {
        UserCapabilities userCapabilities = mock( UserCapabilities.class );
        UserCapabilities.INSTANCE = userCapabilities;
        when( userCapabilities.hasCapability( Capability.SHOW_CREATE_NEW_ASSET ) ).thenReturn( canMakeNewAssets );
    }

    // TODO: global area -Rikkola-
    // TODO: Test that there is a handler for refresh module event -Rikkola-

}
