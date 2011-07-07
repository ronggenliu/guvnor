/*
 * Copyright 2011 JBoss Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.drools.guvnor.client.explorer.navigation;

import org.drools.guvnor.client.configurations.Capability;
import org.drools.guvnor.client.configurations.UserCapabilities;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class BrowseTreeBuilderTest {

    private BrowseTreeBuilder builder;
    private BrowseHeaderView stackItemHeaderView;
    private BrowseTreeView browseTreeView;

    @Before
    public void setUp() throws Exception {
        builder = new BrowseTreeBuilder();
        NavigationViewFactory navigationViewFactory = setUpNavigationFactory();
        stackItemHeaderView = setUpHeaderView( navigationViewFactory );
        browseTreeView = setUpContentView( navigationViewFactory );
    }

    @Test
    public void testBuildPermission() throws Exception {
        assertTrue( builder.hasPermissionToBuild() );
    }

    @Test
    public void testCheckHeader() throws Exception {
        assertEquals( stackItemHeaderView, builder.getHeader() );
    }

//    @Test
//    public void testCheckContent() throws Exception {
//
//        builder.createNewBrowseTree();
//        assertEquals( browseTreeView, builder.getContent() );
//
//        verify( browseTreeView, never() ).addFind();
//        verify( browseTreeView, never() ).addRootStateTreeItem();
//        verify( browseTreeView, never() ).addRootCategoryTreeItem();
//    }

    // TODO: the following is too fancy, what is built into the tree item should be decided in the item -Rikkola-
//    @Test
//    public void testBuildFind() throws Exception {
//        builder.createNewBrowseTree();
//        builder.buildFind();
//        assertEquals(browseTreeView, builder.getContent());
//
//        verify(browseTreeView).addFind();
//        verify(browseTreeView, never()).addRootStateTreeItem();
//        verify(browseTreeView, never()).addRootCategoryTreeItem();
//    }
//
//    @Test
//    public void testBuildStateTreeItem() throws Exception {
//        builder.createNewBrowseTree();
//        builder.buildStateTreeItem();
//        assertEquals(browseTreeView, builder.getContent());
//
//        verify(browseTreeView, never()).addFind();
//        verify(browseTreeView).addRootStateTreeItem();
//        verify(browseTreeView, never()).addRootCategoryTreeItem();
//    }
//
//    @Test
//    public void testBuildCategoriesTreeItem() throws Exception {
//        builder.createNewBrowseTree();
//        builder.buildCategoriesTreeItem();
//        assertEquals(browseTreeView, builder.getContent());
//
//        verify(browseTreeView, never()).addFind();
//        verify(browseTreeView, never()).addRootStateTreeItem();
//        verify(browseTreeView).addRootCategoryTreeItem();
//    }

    private NavigationViewFactory setUpNavigationFactory() {
        NavigationViewFactory navigationViewFactory = mock( NavigationViewFactory.class );
        builder.setViewFactory( navigationViewFactory );
        return navigationViewFactory;
    }

    private BrowseTreeView setUpContentView( NavigationViewFactory navigationViewFactory ) {
        BrowseTreeView browseTreeView = mock( BrowseTreeView.class );
        when( navigationViewFactory.getBrowseTreeView() ).thenReturn( browseTreeView );
        return browseTreeView;
    }

    private BrowseHeaderView setUpHeaderView( NavigationViewFactory navigationViewFactory ) {
        BrowseHeaderView stackItemHeaderView = mock( BrowseHeaderView.class );
        when( navigationViewFactory.getBrowseHeaderView() ).thenReturn( stackItemHeaderView );
        return stackItemHeaderView;
    }
}
