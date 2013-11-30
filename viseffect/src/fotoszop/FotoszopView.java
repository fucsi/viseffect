/*
 * FotoszopView.java
 */

package fotoszop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.Timer;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.TaskMonitor;

import fotoszop.gui.WindowManager;
import fotoszop.tools.basic.Mirror;
import fotoszop.tools.basic.Negative;
import fotoszop.tools.basic.Zoom;
//import fotoszop.tools.fourier.userinterface.FFTGUI;
import fotoszop.tools.morfo.gui.InterfejsMorfologia;

/**
 * The application's main frame.
 */
public class FotoszopView extends FrameView {

    public WindowManager mgr;

    public FotoszopView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        mgr = WindowManager.getInstance();
        mgr.setDesktopPane(jDesktopPane1);
        List<JMenu> menuList = mgr.getMenus();


        for (int i = 0; i < menuList.size(); i++) {
            JMenu jMenu = menuList.get(i);
            menuBar.add(jMenu);
        }

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = FotoszopApp.getApplication().getMainFrame();
            aboutBox = new FotoszopAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        FotoszopApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jDialog1 = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mainPanelKeyPressed(evt);
            }
        });

        jDesktopPane1.setName("jDesktopPane1"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(fotoszop.FotoszopApp.class).getContext().getResourceMap(FotoszopView.class);
        
        jButton1.setText(resourceMap.getString("openBtn.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("openBtn"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.setIcon(resourceMap.getIcon("icons.open"));
        jButton1.setSize(100, 32);
        jToolBar1.add(jButton1);

        jButton2.setText(resourceMap.getString("saveBtn.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("saveBtn"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.setIcon(resourceMap.getIcon("icons.save"));
        jToolBar1.add(jButton2);
        
        JButton saveAs = new JButton();
        saveAs.setText(resourceMap.getString("saveAsBtn.text")); // NOI18N
        saveAs.setFocusable(false);
        saveAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveAs.setName("saveAsBtn"); // NOI18N
        saveAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        saveAs.setIcon(resourceMap.getIcon("icons.saveas"));
        jToolBar1.add(saveAs);

        jToolBar1.add(new javax.swing.JToolBar.Separator());
        
        org.jdesktop.application.ResourceMap resourceMapOperations = org.jdesktop.application.Application
		.getInstance(fotoszop.FotoszopApp.class).getContext()
		.getResourceMap(FotoszopAboutBox.class);
        
        JButton negative = new JButton();
        negative.setText(resourceMap.getString("negative.text")); // NOI18N
        negative.setFocusable(false);
        negative.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        negative.setName("negative"); // NOI18N
        negative.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        negative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new Negative().actionPerformed(evt);
            }
        });
        negative.setIcon(resourceMapOperations.getIcon("icons.negative"));
        jToolBar1.add(negative);
        
        
        JButton mirror = new JButton();
        mirror.setText(resourceMap.getString("mirror.text")); // NOI18N
        mirror.setFocusable(false);
        mirror.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mirror.setName("mirror"); // NOI18N
        mirror.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mirror.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new Mirror().actionPerformed(evt);
            }
        });
        mirror.setIcon(resourceMapOperations.getIcon("icons.mirror"));
        jToolBar1.add(mirror);
        
        JButton zoom = new JButton();
        zoom.setText(resourceMap.getString("zoom.text")); // NOI18N
        zoom.setFocusable(false);
        zoom.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        zoom.setName("zoom"); // NOI18N
        zoom.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        zoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new Zoom().actionPerformed(evt);
            }
        });
        zoom.setIcon(resourceMapOperations.getIcon("icons.zoom"));
        jToolBar1.add(zoom);
        
        jToolBar1.add(new javax.swing.JToolBar.Separator());
        
        JButton morpho = new JButton();
        morpho.setText(resourceMap.getString("morpho.text")); // NOI18N
        morpho.setFocusable(false);
        morpho.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        morpho.setName("morpho"); // NOI18N
        morpho.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        morpho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	new InterfejsMorfologia().actionPerformed(evt);
            }
        });
        morpho.setIcon(resourceMapOperations.getIcon("icons.morpho"));
        jToolBar1.add(morpho);
        
        jToolBar1.add(new javax.swing.JToolBar.Separator());
        
        //mapa akcji
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(fotoszop.FotoszopApp.class).getContext().getActionMap(FotoszopView.class, this);
        
        JButton exit = new JButton();
        exit.setText(resourceMap.getString("exit.text")); // NOI18N
        exit.setFocusable(false);
        exit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exit.setName("zoom"); // NOI18N
        exit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exit.setAction(actionMap.get("quit"));
        exit.setIcon(resourceMap.getIcon("icons.exit"));
        jToolBar1.add(exit);
        
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.setIcon(resourceMap.getIcon("icons.save"));
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.setIcon(resourceMap.getIcon("icons.open"));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);
        
        fileMenu.add(new JSeparator());
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("icons.exit"));
        fileMenu.add(exitMenuItem);
        
        menuBar.add(fileMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 386, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jDialog1.setAlwaysOnTop(true);
        jDialog1.setFocusable(false);
        jDialog1.setModal(true);
        jDialog1.setName("jDialog1"); // NOI18N
        jDialog1.setResizable(false);
        jDialog1.setUndecorated(true);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jList1.setModel(new javax.swing.AbstractListModel() {
			private static final long serialVersionUID = 1L;
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setName("jList1"); // NOI18N
        jScrollPane1.setViewportView(jList1);

        jPanel1.setName("jPanel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 311, Short.MAX_VALUE)
        );

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        mgr.openImage();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        mgr.openImage();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mainPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mainPanelKeyPressed
        
    }//GEN-LAST:event_mainPanelKeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        mgr.saveImage();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        mgr.saveImage();
    }//GEN-LAST:event_jButton2ActionPerformed
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        mgr.saveImageAs();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}