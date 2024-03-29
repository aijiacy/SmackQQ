/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.ninepatch;

import com.alee.extended.drag.FileDragAndDropHandler;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.painter.AlphaLayerPainter;
import com.alee.extended.painter.ColorPainter;
import com.alee.extended.panel.ResizablePanel;
import com.alee.extended.panel.WebButtonGroup;
import com.alee.extended.statusbar.WebStatusBar;
import com.alee.extended.tree.WebFileTree;
import com.alee.global.GlobalConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.colorchooser.WebColorChooserDialog;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.slider.WebSlider;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.style.skin.ninepatch.NPLabelPainter;
import com.alee.utils.*;
import com.alee.utils.ninepatch.NinePatchInterval;
import com.alee.utils.ninepatch.NinePatchIntervalType;
import com.alee.utils.swing.DialogOptions;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a simple panel that contains fully-functional nine-patch image format editor.
 *
 * @author Mikle Garin
 * @see NinePatchEditor
 * @see NinePatchEditorDialog
 */

public class NinePatchEditorPanel extends WebPanel
{
    // todo Should make proper toolbar elements enable/disable
    // todo Multi-image editing

    public static final ImageIcon OPEN_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/open.png" ) );
    public static final ImageIcon SAVE_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/save.png" ) );
    public static final ImageIcon SAVE_AS_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/saveas.png" ) );
    public static final ImageIcon UNDO_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/undo.png" ) );
    public static final ImageIcon REDO_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/redo.png" ) );

    public static final ImageIcon COPY_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/copy.png" ) );
    public static final ImageIcon PASTE_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/paste.png" ) );
    public static final ImageIcon ROTATE_CCW_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/rotate_ccw.png" ) );
    public static final ImageIcon ROTATE_CW_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/rotate_cw.png" ) );
    public static final ImageIcon ROTATE_180_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/rotate_180.png" ) );

    public static final ImageIcon GUIDES_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/guides.png" ) );
    public static final ImageIcon RULER_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/ruler.png" ) );
    public static final ImageIcon STRETCH_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/stretch.png" ) );
    public static final ImageIcon CONTENT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/content.png" ) );
    public static final ImageIcon RULER_CURSOR_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/ruler_cursor.png" ) );
    public static final ImageIcon AREA_CURSOR_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/area_cursor.png" ) );
    public static final ImageIcon MIN_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/min.png" ) );
    public static final ImageIcon MAX_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/max.png" ) );

    public static final ImageIcon ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/icon.png" ) );
    public static final ImageIcon SHOW_ICON_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/show_icon.png" ) );
    public static final ImageIcon SHOW_TEXT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/show_text.png" ) );
    public static final ImageIcon FOREGROUND_COLOR_ICON =
            new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/foreground_color.png" ) );
    public static final ImageIcon TRANSPARENT_ICON = new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/transparent.png" ) );
    public static final ImageIcon BACKGROUND_COLOR_ICON =
            new ImageIcon ( NinePatchEditorPanel.class.getResource ( "icons/background_color.png" ) );

    private final List<ChangeListener> changeListeners = new ArrayList<ChangeListener> ( 1 );

    private String imageSrc = null;

    private final WebFileTree fileTree;
    private NinePatchEditor ninePatchEditor;

    private WebToolBar toolBar;
    private WebButton save;
    private WebButton saveAs;

    private ChangeListener changeListener;
    private ZoomChangeListener zoomChangeListener;

    private AlphaLayerPainter abp;
    private ColorPainter colorPainter;

    private WebPanel previewPanel;
    private WebLabel preview;

    private boolean openFromTreeEnabled = true;

    public NinePatchEditorPanel ()
    {
        super ();

        initializeAliases ();

        setLayout ( new BorderLayout () );

        fileTree = new WebFileTree ();
        fileTree.setSelectionStyle ( TreeSelectionStyle.single );
        fileTree.setSelectionMode ( TreeSelectionModel.SINGLE_TREE_SELECTION );
        fileTree.setFileFilter ( GlobalConstants.IMAGES_AND_FOLDERS_FILTER );
        fileTree.addTreeSelectionListener ( new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                if ( openFromTreeEnabled && fileTree.getSelectionCount () > 0 )
                {
                    openImage ( fileTree.getSelectedFile () );
                }
            }
        } );
        fileTree.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( final MouseEvent e )
            {
                if ( e.getClickCount () == 2 && openFromTreeEnabled &&
                        fileTree.getSelectionCount () > 0 )
                {
                    openImage ( fileTree.getSelectedFile () );
                }
            }
        } );
        fileTree.setTransferHandler ( new TransferHandler ()
        {
            @Override
            public int getSourceActions ( final JComponent c )
            {
                return TransferHandler.COPY;
            }

            @Override
            protected Transferable createTransferable ( final JComponent c )
            {
                return fileTree.getSelectionCount () > 0 ? new StringSelection ( fileTree.getSelectedFile ().getAbsolutePath () ) : null;
            }
        } );

        final WebScrollPane filesView = new WebScrollPane ( fileTree, false );
        filesView.setMinimumWidth ( 200 );
        filesView.setPreferredHeight ( 0 );
        //        filesView.setBorder ( BorderFactory.createMatteBorder ( 0, 0, 0, 1, WebToolBarStyle.borderColor ) );

        final WebSplitPane previewSplit = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        previewSplit.setLeftComponent ( createEditorPanel () );
        previewSplit.setRightComponent ( createPreviewPanel () );
        previewSplit.setOneTouchExpandable ( true );
        previewSplit.setContinuousLayout ( true );
        previewSplit.setResizeWeight ( 1 );

        final WebSplitPane filesSplit = new WebSplitPane ( WebSplitPane.HORIZONTAL_SPLIT );
        filesSplit.setLeftComponent ( filesView );
        filesSplit.setRightComponent ( previewSplit );
        filesSplit.setOneTouchExpandable ( true );
        filesSplit.setContinuousLayout ( true );
        filesSplit.setResizeWeight ( 0 );
        add ( filesSplit, BorderLayout.CENTER );

        final Integer fsl = SettingsManager.get ( "NinePatchEditor", "filesSplitLocation", ( Integer ) null );
        filesSplit.setDividerLocation ( fsl != null ? fsl : 230 );
        filesSplit.addPropertyChangeListener ( WebSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent pce )
            {
                SettingsManager.set ( "NinePatchEditor", "filesSplitLocation", filesSplit.getDividerLocation () );
            }
        } );

        final Integer psl = SettingsManager.get ( "NinePatchEditor", "splitLocation", ( Integer ) null );
        if ( psl != null )
        {
            previewSplit.setDividerLocation ( psl );
        }
        previewSplit.addPropertyChangeListener ( WebSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent pce )
            {
                SettingsManager.set ( "NinePatchEditor", "splitLocation", previewSplit.getDividerLocation () );
            }
        } );

        LanguageManager.updateComponentsTree ( NinePatchEditorPanel.this );
    }

    private Component createEditorPanel ()
    {
        final WebPanel editorPanel = new WebPanel ();

        //

        toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setFloatable ( false );
        editorPanel.add ( toolBar, BorderLayout.NORTH );

        final WebButton open = new WebButton ( OPEN_ICON );
        open.setLanguage ( "weblaf.ex.npeditor.openImage" );
        open.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_O );
        open.setRolloverDecoratedOnly ( true );
        open.addActionListener ( new ActionListener ()
        {
            private WebFileChooser wfc = null;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( wfc == null )
                {
                    wfc = new WebFileChooser ( imageSrc );
                    wfc.setDialogTitleKey ( "weblaf.ex.npeditor.openImage.title" );
                    wfc.setMultiSelectionEnabled ( false );
                    wfc.addChoosableFileFilter ( GlobalConstants.IMAGES_FILTER );
                    wfc.setFileFilter ( GlobalConstants.IMAGES_FILTER );
                }
                if ( imageSrc != null )
                {
                    wfc.setSelectedFile ( imageSrc );
                }
                if ( wfc.showOpenDialog ( SwingUtils.getWindowAncestor ( NinePatchEditorPanel.this ) ) == WebFileChooser.APPROVE_OPTION )
                {
                    openImage ( wfc.getSelectedFile () );
                }
            }
        } );
        toolBar.add ( open );

        toolBar.addSeparator ();

        save = new WebButton ( SAVE_ICON );
        save.setLanguage ( "weblaf.ex.npeditor.saveImage" );
        save.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_S );
        save.setRolloverDecoratedOnly ( true );
        save.setEnabled ( false );
        save.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( imageSrc != null )
                {
                    try
                    {
                        saveImage ( new File ( imageSrc ) );
                    }
                    catch ( final IOException e1 )
                    {
                        e1.printStackTrace ();
                    }
                }
            }
        } );
        toolBar.add ( save );

        saveAs = new WebButton ( SAVE_AS_ICON );
        saveAs.setLanguage ( "weblaf.ex.npeditor.saveImageAs" );
        saveAs.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_SHIFT_S );
        saveAs.setRolloverDecoratedOnly ( true );
        saveAs.addActionListener ( new ActionListener ()
        {
            private WebFileChooser wfc = null;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( wfc == null )
                {
                    wfc = new WebFileChooser ( imageSrc );
                    wfc.setDialogTitleKey ( "weblaf.ex.npeditor.saveImageAs.title" );
                    wfc.setMultiSelectionEnabled ( false );
                }
                if ( imageSrc != null )
                {
                    wfc.setSelectedFile ( imageSrc );
                }
                if ( wfc.showSaveDialog ( SwingUtils.getWindowAncestor ( NinePatchEditorPanel.this ) ) == WebFileChooser.APPROVE_OPTION )
                {
                    try
                    {
                        saveImage ( wfc.getSelectedFile () );
                        save.setEnabled ( true );
                    }
                    catch ( final IOException e1 )
                    {
                        e1.printStackTrace ();
                    }
                }
            }
        } );
        toolBar.add ( saveAs );

        toolBar.addSeparator ();

        final WebButton undo = new WebButton ( UNDO_ICON );
        undo.setLanguage ( "weblaf.ex.npeditor.undo" );
        undo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_Z );
        undo.setRolloverDecoratedOnly ( true );
        undo.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                ninePatchEditor.undo ();
            }
        } );
        toolBar.add ( undo );

        final WebButton redo = new WebButton ( REDO_ICON );
        redo.setLanguage ( "weblaf.ex.npeditor.redo" );
        redo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_R ).setHidden ( true );
        redo.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_SHIFT_Z );
        redo.setRolloverDecoratedOnly ( true );
        redo.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                ninePatchEditor.redo ();
            }
        } );
        toolBar.add ( redo );

        //

        ninePatchEditor = new NinePatchEditor ();
        ninePatchEditor.setTransferHandler ( new FileDragAndDropHandler ()
        {
            @Override
            public boolean filesDropped ( final List<File> files )
            {
                if ( files != null )
                {
                    for ( final File file : files )
                    {
                        if ( ImageUtils.isImageLoadable ( file.getName () ) )
                        {
                            openImage ( file );
                            return true;
                        }
                    }
                }
                return false;
            }
        } );
        editorPanel.add ( ninePatchEditor.getView (), BorderLayout.CENTER );

        final WebButton copy = new WebButton ( COPY_ICON );
        copy.setLanguage ( "weblaf.ex.npeditor.copyInfo" );
        copy.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_C );
        copy.setRolloverDecoratedOnly ( true );
        copy.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    SystemUtils.copyToClipboard ( XmlUtils.toXML ( ninePatchEditor.getNinePatchInfo () ) );
                }
            }
        } );
        toolBar.addToEnd ( copy );

        final WebButton paste = new WebButton ( PASTE_ICON );
        paste.setLanguage ( "weblaf.ex.npeditor.pasteInfo" );
        paste.addHotkey ( NinePatchEditorPanel.this, Hotkey.CTRL_V );
        paste.setRolloverDecoratedOnly ( true );
        paste.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    final String xml = SystemUtils.getStringFromClipboard ();
                    if ( xml != null )
                    {
                        // Retrieving data from xml
                        final NinePatchInfo info = XmlUtils.fromXML ( xml );

                        // Restoring data if it fits size
                        ninePatchEditor.setNinePatchInfo ( info );
                    }
                }
            }
        } );
        toolBar.addToEnd ( paste );

        toolBar.addSeparatorToEnd ();

        final WebButton rotateCCW = new WebButton ( ROTATE_CCW_ICON );
        rotateCCW.setLanguage ( "weblaf.ex.npeditor.rotateCCW" );
        rotateCCW.setRolloverDecoratedOnly ( true );
        rotateCCW.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    // Replacing icon
                    ninePatchEditor.setNinePatchIcon ( NinePatchUtils.rotateIcon90CCW ( ninePatchEditor.getNinePatchIcon () ) );
                }
            }
        } );
        toolBar.addToEnd ( rotateCCW );

        final WebButton rotateCW = new WebButton ( ROTATE_CW_ICON );
        rotateCW.setLanguage ( "weblaf.ex.npeditor.rotateCW" );
        rotateCW.setRolloverDecoratedOnly ( true );
        rotateCW.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    // Replacing icon
                    ninePatchEditor.setNinePatchIcon ( NinePatchUtils.rotateIcon90CW ( ninePatchEditor.getNinePatchIcon () ) );
                }
            }
        } );
        toolBar.addToEnd ( rotateCW );

        final WebButton rotate180 = new WebButton ( ROTATE_180_ICON );
        rotate180.setLanguage ( "weblaf.ex.npeditor.rotate180" );
        rotate180.setRolloverDecoratedOnly ( true );
        rotate180.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( !ninePatchEditor.isSomeDragged () )
                {
                    // Replacing icon
                    ninePatchEditor.setNinePatchIcon ( NinePatchUtils.rotateIcon180 ( ninePatchEditor.getNinePatchIcon () ) );
                }
            }
        } );
        toolBar.addToEnd ( rotate180 );

        //

        final WebStatusBar bottomToolBar = new WebStatusBar ();
        editorPanel.add ( bottomToolBar, BorderLayout.SOUTH );

        final boolean sgs = SettingsManager.get ( "NinePatchEditor", "showSpacing", true );
        final WebToggleButton showGuidesSpacing = new WebToggleButton ( GUIDES_ICON );
        showGuidesSpacing.setLanguage ( "weblaf.ex.npeditor.showSpacing" );
        showGuidesSpacing.setRolloverDecoratedOnly ( true );
        showGuidesSpacing.setSelected ( sgs );
        ninePatchEditor.setShowGuideSpacing ( sgs );
        showGuidesSpacing.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean sgs = showGuidesSpacing.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "showSpacing", sgs );
                ninePatchEditor.setShowGuideSpacing ( sgs );
            }
        } );

        final boolean sr = SettingsManager.get ( "NinePatchEditor", "showRuler", true );
        final WebToggleButton showRuler = new WebToggleButton ( RULER_ICON );
        showRuler.setLanguage ( "weblaf.ex.npeditor.showRuler" );
        showRuler.setRolloverDecoratedOnly ( true );
        showRuler.setSelected ( sr );
        ninePatchEditor.setShowRuler ( sr );
        showRuler.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean sr = showRuler.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "showRuler", sr );
                ninePatchEditor.setShowRuler ( sr );
            }
        } );

        final boolean fc = SettingsManager.get ( "NinePatchEditor", "fillContent", true );
        final WebToggleButton fillContent = new WebToggleButton ( CONTENT_ICON );
        fillContent.setLanguage ( "weblaf.ex.npeditor.fillContent" );
        fillContent.setRolloverDecoratedOnly ( true );
        fillContent.setSelected ( fc );
        ninePatchEditor.setFillContentArea ( fc );
        fillContent.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean fc = fillContent.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "fillContent", fc );
                ninePatchEditor.setFillContentArea ( fc );
            }
        } );

        final boolean fs = SettingsManager.get ( "NinePatchEditor", "fillStretch", true );
        final WebToggleButton fillStretch = new WebToggleButton ( STRETCH_ICON );
        fillStretch.setLanguage ( "weblaf.ex.npeditor.fillStretch" );
        fillStretch.setRolloverDecoratedOnly ( true );
        fillStretch.setSelected ( fs );
        ninePatchEditor.setFillStretchAreas ( fs );
        fillStretch.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean fs = fillStretch.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "fillStretch", fs );
                ninePatchEditor.setFillStretchAreas ( fs );
            }
        } );

        final boolean rc = SettingsManager.get ( "NinePatchEditor", "rulerCursor", true );
        final WebToggleButton rulerCursor = new WebToggleButton ( RULER_CURSOR_ICON );
        rulerCursor.setLanguage ( "weblaf.ex.npeditor.rulerCursor" );
        rulerCursor.setRolloverDecoratedOnly ( true );
        rulerCursor.setSelected ( rc );
        ninePatchEditor.setShowRulerCursorPosition ( rc );
        rulerCursor.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean rc = rulerCursor.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "rulerCursor", rc );
                ninePatchEditor.setShowRulerCursorPosition ( rc );
            }
        } );

        final boolean ac = SettingsManager.get ( "NinePatchEditor", "areaCursor", false );
        final WebToggleButton areaCursor = new WebToggleButton ( AREA_CURSOR_ICON );
        areaCursor.setLanguage ( "weblaf.ex.npeditor.areaCursor" );
        areaCursor.setRolloverDecoratedOnly ( true );
        areaCursor.setSelected ( ac );
        ninePatchEditor.setShowAreaCursorPosition ( ac );
        areaCursor.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean ac = areaCursor.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "areaCursor", ac );
                ninePatchEditor.setShowAreaCursorPosition ( ac );
            }
        } );

        final WebSlider zoomSlider = new WebSlider ( NinePatchEditor.MIN_ZOOM, NinePatchEditor.MAX_ZOOM, ninePatchEditor.getZoom () );
        zoomSlider.setPreferredWidth ( 170 );
        zoomSlider.setAngledThumb ( false );
        zoomSlider.setPaintTicks ( false );
        zoomSlider.setPaintLabels ( false );
        changeListener = new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                ninePatchEditor.removeZoomChangeListener ( zoomChangeListener );
                ninePatchEditor.setZoom ( zoomSlider.getValue () );
                ninePatchEditor.addZoomChangeListener ( zoomChangeListener );
            }
        };
        zoomSlider.addChangeListener ( changeListener );
        zoomChangeListener = new ZoomChangeListener ()
        {
            @Override
            public void zoomChanged ()
            {
                zoomSlider.removeChangeListener ( changeListener );
                zoomSlider.setValue ( ninePatchEditor.getZoom () );
                zoomSlider.addChangeListener ( changeListener );
            }
        };
        ninePatchEditor.addZoomChangeListener ( zoomChangeListener );

        final WebButton minZoom = new WebButton ( MIN_ICON );
        minZoom.setRolloverDecoratedOnly ( true );
        minZoom.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                zoomSlider.setValue ( NinePatchEditor.MIN_ZOOM );
            }
        } );

        final WebButton maxZoom = new WebButton ( MAX_ICON );
        maxZoom.setRolloverDecoratedOnly ( true );
        maxZoom.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                zoomSlider.setValue ( NinePatchEditor.MAX_ZOOM );
            }
        } );

        bottomToolBar.add ( showGuidesSpacing );
        bottomToolBar.add ( showRuler );
        bottomToolBar.addSeparator ();
        bottomToolBar.add ( fillContent );
        bottomToolBar.add ( fillStretch );
        bottomToolBar.addSeparator ();
        bottomToolBar.add ( rulerCursor );
        bottomToolBar.add ( areaCursor );
        bottomToolBar.addToEnd ( minZoom );
        bottomToolBar.addToEnd ( zoomSlider );
        bottomToolBar.addToEnd ( maxZoom );

        return editorPanel;
    }

    private static boolean aliasesInitialized = false;

    private static void initializeAliases ()
    {
        if ( !aliasesInitialized )
        {
            aliasesInitialized = true;
            XmlUtils.processAnnotations ( NinePatchInfo.class );
            XmlUtils.processAnnotations ( NinePatchInterval.class );
            XmlUtils.processAnnotations ( NinePatchIntervalType.class );
        }
    }

    public void openImage ( final File file )
    {
        try
        {
            // Ignore same file opening
            if ( imageSrc != null && ( file.getAbsolutePath ().equals ( imageSrc ) ) )
            {
                return;
            }

            // Ignore unloadable images
            if ( file.isDirectory () || !ImageUtils.isImageLoadable ( file.getName () ) )
            {
                return;
            }

            // Load image (avoiding Toolkit cache)
            final Image image = Toolkit.getDefaultToolkit ().createImage ( file.getAbsolutePath () );
            if ( image == null )
            {
                return;
            }

            // Check if changes save needed
            if ( !continueAfterSave () )
            {
                return;
            }

            // Loading image fully through ImageIcon MediaTracker
            final BufferedImage bi = ImageUtils.getBufferedImage ( new ImageIcon ( image ) );

            // Open image file
            ninePatchEditor.setNinePatchImage ( bi );
            imageSrc = file.getAbsolutePath ();
            save.setEnabled ( true );

            // Show location in tree
            openFromTreeEnabled = false;
            fileTree.setSelectedFile ( file );
            fileTree.expandToFile ( file, true, false, new Runnable ()
            {
                @Override
                public void run ()
                {
                    openFromTreeEnabled = true;
                }
            } );

            // Inform about changes
            fireStateChanged ();
        }
        catch ( final Throwable e )
        {
            //
        }
    }

    public boolean continueAfterSave ()
    {
        if ( ninePatchEditor.isChanged () )
        {
            final String message = LanguageManager.get ( "weblaf.ex.npeditor.saveChanges.text" );
            final String title = LanguageManager.get ( "weblaf.ex.npeditor.saveChanges.title" );
            final int option = WebOptionPane.YES_NO_CANCEL_OPTION;
            final int messageType = WebOptionPane.QUESTION_MESSAGE;
            final int confirm = WebOptionPane.showConfirmDialog ( this, message, title, option, messageType );

            if ( confirm == WebOptionPane.YES_OPTION )
            {
                // Save changes before open
                if ( save.isEnabled () )
                {
                    save.doClick ();
                }
                else
                {
                    saveAs.doClick ();
                }

                // Save operation cancelled or failed
                if ( ninePatchEditor.isChanged () )
                {
                    return false;
                }
            }
            else if ( confirm == WebOptionPane.CANCEL_OPTION )
            {
                // Cancel open
                return false;
            }
        }
        return true;
    }

    private WebPanel createPreviewPanel ()
    {
        previewPanel = new WebPanel ()
        {
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.width = Math.max ( 230, ps.width );
                ps.height = 400;
                return ps;
            }
        };
        previewPanel.setLayout ( new TableLayout ( new double[][]{ { TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL },
                { TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL } } ) );
        previewPanel.setPainter ( abp );

        preview = new WebLabel ( "", WebLabel.CENTER );
        previewPanel.add ( new ResizablePanel ( preview ), "1,2" );

        updatePreviews ();
        getNinePatchEditor ().addChangeListener ( new ChangeListener ()
        {
            @Override
            public void stateChanged ( final ChangeEvent e )
            {
                updatePreviews ();
            }
        } );

        //

        final WebToolBar toolBar = new WebToolBar ( WebToolBar.HORIZONTAL );
        toolBar.setToolbarStyle ( ToolbarStyle.attached );
        toolBar.setFloatable ( false );
        previewPanel.add ( toolBar, "0,0,2,0" );

        final boolean si = SettingsManager.get ( "NinePatchEditor", "preview.showIcon", false );
        preview.setIcon ( si ? ICON : null );

        final WebToggleButton showIcon = new WebToggleButton ( SHOW_ICON_ICON );
        showIcon.setLanguage ( "weblaf.ex.npeditor.preview.showIcon" );
        showIcon.setRolloverDecoratedOnly ( true );
        showIcon.setSelected ( si );
        showIcon.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean show = showIcon.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "preview.showIcon", show );
                preview.setIcon ( show ? ICON : null );
            }
        } );
        toolBar.add ( showIcon );

        final boolean st = SettingsManager.get ( "NinePatchEditor", "preview.showText", true );
        final WebToggleButton showText = new WebToggleButton ( SHOW_TEXT_ICON );
        showText.setLanguage ( "weblaf.ex.npeditor.preview.showText" );
        showText.setCursor ( Cursor.getDefaultCursor () );
        showText.setDrawFocus ( false );
        showText.setRolloverDecoratedOnly ( true );
        showText.setRolloverDarkBorderOnly ( false );
        showText.setDrawRight ( false );
        showText.setDrawRightLine ( true );
        showText.setShadeWidth ( 0 );
        showText.setSelected ( st );

        final String defaultPreviewText = LanguageManager.get ( "weblaf.ex.npeditor.preview.text" );
        final String previewText = SettingsManager.get ( "NinePatchEditor", "preview.text", defaultPreviewText );
        preview.setText ( st ? parseToMultilineHtml ( previewText ) : "" );

        final WebTextField textField = new WebTextField ( 8 );
        textField.setText ( previewText );
        textField.setHorizontalAlignment ( WebTextField.CENTER );
        textField.setDrawFocus ( false );
        textField.setEditable ( st );
        textField.setMargin ( -1 );
        textField.addCaretListener ( new CaretListener ()
        {
            @Override
            public void caretUpdate ( final CaretEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.text", textField.getText () );
                preview.setText ( showText.isSelected () ? parseToMultilineHtml ( textField.getText () ) : "" );
            }
        } );
        showText.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final boolean show = showText.isSelected ();
                SettingsManager.set ( "NinePatchEditor", "preview.showText", show );
                textField.setEditable ( show );
                preview.setText ( show ? parseToMultilineHtml ( textField.getText () ) : "" );
            }
        } );
        textField.setLeadingComponent ( showText );
        toolBar.addFill ( textField );


        final WebButton chooseColor = new WebButton ( FOREGROUND_COLOR_ICON );
        chooseColor.setLanguage ( "weblaf.ex.npeditor.preview.foregroundColor" );
        preview.setForeground ( SettingsManager.get ( "NinePatchEditor", "preview.foregroundColor", Color.WHITE ) );
        chooseColor.setCursor ( Cursor.getDefaultCursor () );
        chooseColor.setDrawFocus ( false );
        chooseColor.setRolloverDecoratedOnly ( true );
        chooseColor.setRolloverDarkBorderOnly ( false );
        chooseColor.setDrawLeft ( false );
        chooseColor.setDrawLeftLine ( true );
        chooseColor.setShadeWidth ( 0 );
        chooseColor.addActionListener ( new ActionListener ()
        {
            private WebColorChooserDialog webColorChooser = null;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( webColorChooser == null )
                {
                    webColorChooser = new WebColorChooserDialog ( SwingUtils.getWindowAncestor ( previewPanel ) );
                }
                webColorChooser.setColor ( preview.getForeground () );
                if ( webColorChooser.showDialog () == DialogOptions.OK_OPTION )
                {
                    final Color color = webColorChooser.getColor ();
                    SettingsManager.set ( "NinePatchEditor", "preview.foregroundColor", color );
                    preview.setForeground ( color );
                }
            }
        } );
        textField.setTrailingComponent ( chooseColor );

        abp = new AlphaLayerPainter ();

        final boolean da = SettingsManager.get ( "NinePatchEditor", "preview.transparentBackground", true );
        final WebToggleButton drawAlphaBackground = new WebToggleButton ( TRANSPARENT_ICON );
        drawAlphaBackground.setLanguage ( "weblaf.ex.npeditor.preview.transparentBackground" );
        drawAlphaBackground.setSelected ( da );
        drawAlphaBackground.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.transparentBackground", true );
                previewPanel.setPainter ( abp );
            }
        } );

        colorPainter = new ColorPainter ( SettingsManager.get ( "NinePatchEditor", "preview.backgroundColor", Color.WHITE ) );

        final WebToggleButton drawColoredBackground = new WebToggleButton ( ImageUtils.createColorIcon ( colorPainter.getColor () ) );
        drawColoredBackground.setLanguage ( "weblaf.ex.npeditor.preview.coloredBackground" );
        drawColoredBackground.setSelected ( !da );
        drawColoredBackground.addActionListener ( new ActionListener ()
        {
            private WebColorChooserDialog webColorChooser = null;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                SettingsManager.set ( "NinePatchEditor", "preview.transparentBackground", false );
                if ( webColorChooser == null )
                {
                    webColorChooser = new WebColorChooserDialog ( SwingUtils.getWindowAncestor ( previewPanel ) );
                }
                webColorChooser.setColor ( colorPainter.getColor () );
                if ( webColorChooser.showDialog () == DialogOptions.OK_OPTION )
                {
                    final Color color = webColorChooser.getColor ();
                    SettingsManager.set ( "NinePatchEditor", "preview.backgroundColor", color );
                    drawColoredBackground.setIcon ( ImageUtils.createColorIcon ( color ) );
                    colorPainter.setColor ( color );
                }
                previewPanel.setPainter ( colorPainter );
            }
        } );

        previewPanel.setPainter ( da ? abp : colorPainter );

        toolBar.addToEnd ( new WebButtonGroup ( true, drawAlphaBackground, drawColoredBackground ) );

        return new WebPanel ( previewPanel );
    }

    private String parseToMultilineHtml ( final String text )
    {
        return "<html><center>" + text.replaceAll ( ";", "<br>" ).replaceAll ( "\\\\n", "<br>" ) +
                "</center></html>";
    }

    private void updatePreviews ()
    {
        preview.setPainter ( new NPLabelPainter ( getNinePatchEditor ().getNinePatchIcon () ) );
        previewPanel.revalidate ();
    }

    private void saveImage ( File imageFile ) throws IOException
    {
        // Checking file name
        final String fullName = getSaveFileName ( imageFile );
        imageFile = new File ( imageFile.getParent (), fullName );
        imageSrc = imageFile.getAbsolutePath ();

        // Saving image
        final BufferedImage image = ninePatchEditor.getNinePatchImage ();
        ImageIO.write ( image, "png", imageFile );

        // Nullify changes
        ninePatchEditor.setChanged ( false );

        // Inform about changes
        fireStateChanged ();
    }

    private String getSaveFileName ( final File imageFile )
    {
        // Adding .9 to file name if it doesn't exist
        final String format = ".png";
        final String subFormat = ".9";
        String fullName = imageFile.getName ();
        if ( fullName.endsWith ( format ) )
        {
            final String name = fullName.substring ( 0, fullName.lastIndexOf ( format ) );
            if ( !name.endsWith ( subFormat ) )
            {
                fullName = name + subFormat + format;
            }
        }
        else if ( fullName.endsWith ( subFormat ) )
        {
            fullName = fullName + format;
        }
        else
        {
            final int dot = fullName.lastIndexOf ( "" );
            if ( dot != -1 )
            {
                fullName = fullName.substring ( 0, dot );
            }
            fullName = fullName + subFormat + format;
        }
        return fullName;
    }

    public NinePatchEditor getNinePatchEditor ()
    {
        return ninePatchEditor;
    }

    public String getImageSrc ()
    {
        return imageSrc;
    }

    public void setNinePatchImage ( final ImageIcon imageIcon )
    {
        setNinePatchImage ( imageIcon.getImage () );
    }

    public void setNinePatchImage ( final Image image )
    {
        setNinePatchImage ( ImageUtils.getBufferedImage ( image ) );
    }

    public void setNinePatchImage ( final BufferedImage ninePatchImage )
    {
        // Check if changes save needed
        if ( !continueAfterSave () )
        {
            return;
        }

        // Open image file
        ninePatchEditor.setNinePatchImage ( ninePatchImage );
        imageSrc = null;
        save.setEnabled ( true );

        // Inform about changes
        fireStateChanged ();
    }

    public BufferedImage getNinePatchImage ()
    {
        return ninePatchEditor.getNinePatchImage ();
    }

    public void setSelectedDirectory ( final File file )
    {
        fileTree.setSelectedFile ( file, true );
    }

    public List<ChangeListener> getChangeListeners ()
    {
        return changeListeners;
    }

    public void addChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    public void removeChangeListener ( final ChangeListener changeListener )
    {
        changeListeners.add ( changeListener );
    }

    private void fireStateChanged ()
    {
        final ChangeEvent changeEvent = new ChangeEvent ( NinePatchEditorPanel.this );
        for ( final ChangeListener listener : CollectionUtils.copy ( changeListeners ) )
        {
            listener.stateChanged ( changeEvent );
        }
    }
}