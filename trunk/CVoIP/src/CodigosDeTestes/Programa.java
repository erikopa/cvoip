/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigosDeTestes;

/**
 *
 * @author Eriko Verissimo
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.swing.JFileChooser;

public class Programa extends javax.swing.JFrame {

    Player player = null;
    Component visualMedia;
    Component controles;
    File arquivo;

    /** Creates new form Programa */
    public Programa() {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        try {
            //arquivo = getFile();
            //System.out.print(arquivo.toString());
            URL url = new URL("file",null,"C:/Eriko/Minhas%20músicas/Clipes/Bon%20Jovi%20-%20Misunderstood.mpg");
            

            player = Manager.createPlayer(url);
            player.realize();
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
    }
    public File getFile()
   {
      JFileChooser fileChooser = new JFileChooser();

      fileChooser.setFileSelectionMode(
         JFileChooser.FILES_ONLY );

      int result = fileChooser.showOpenDialog( this );

      if ( result == JFileChooser.CANCEL_OPTION )
         return null;

      else
         return fileChooser.getSelectedFile();
   }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">   
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panelVideo = new javax.swing.JPanel();
        panelControle = new javax.swing.JPanel();
        btnVoltar = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnProximo = new javax.swing.JButton();
        progressoPlayer = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("::. JMF Programa .::");
        panelVideo.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(panelVideo, java.awt.BorderLayout.CENTER);

        panelControle.setLayout(new java.awt.GridBagLayout());

        panelControle.setBackground(new java.awt.Color(255, 255, 255));
        panelControle.setPreferredSize(new java.awt.Dimension(10, 80));
        //btnVoltar.setIcon(new javax.swing.ImageIcon("D:\\Video\\voltar.gif"));
        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panelControle.add(btnVoltar, gridBagConstraints);

        //btnPlay.setIcon(new javax.swing.ImageIcon("D:\\Video\\play24.gif"));
        btnPlay.setText("Play");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panelControle.add(btnPlay, gridBagConstraints);

        //btnPause.setIcon(new javax.swing.ImageIcon("D:\\Video\\pause24.gif"));
        btnPause.setText("Pause");
        btnPause.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPauseActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panelControle.add(btnPause, gridBagConstraints);

        //btnStop.setIcon(new javax.swing.ImageIcon("D:\\Video\\stop24.gif"));
        btnStop.setText("Stop");
        btnStop.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panelControle.add(btnStop, gridBagConstraints);

        //btnProximo.setIcon(new javax.swing.ImageIcon("D:\\Video\\fastForward24.gif"));
        btnProximo.setText("Proximo");
        btnProximo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panelControle.add(btnProximo, gridBagConstraints);

        progressoPlayer.setBackground(new java.awt.Color(255, 255, 255));
        progressoPlayer.setValue(0);
        panelControle.add(progressoPlayer, new java.awt.GridBagConstraints());

        getContentPane().add(panelControle, java.awt.BorderLayout.SOUTH);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 800) / 2, (screenSize.height - 613) / 2, 800, 613);
    }
    // </editor-fold>   

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {
        player.stop();
    }

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {
        
        
        

    }

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            visualMedia = player.getVisualComponent();
            controles = player.getControlPanelComponent();

            if (visualMedia != null) {
                panelVideo.add(visualMedia, java.awt.BorderLayout.CENTER);
                
                panelVideo.revalidate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.start();
    }

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:   
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Programa().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify   
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnProximo;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JPanel panelControle;
    private javax.swing.JPanel panelVideo;
    private javax.swing.JSlider progressoPlayer;
    // End of variables declaration   
}  

