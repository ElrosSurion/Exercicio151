
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Delicious.java
 *
 * Created on 12-mar-2014, 19:47:37
 */

/**
 *
 * @author ANTONIOFA
 */
class Usuario {
    private String login, clave;
    
    public Usuario(String login, String clave) {
        this.login = login;
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    public static boolean Existe(String login, String clave) {
        boolean resultado = false;
        try {
            PreparedStatement PSSELECTOR = Delicious.connection.prepareStatement("SELECT * FROM usuarios WHERE login = ? AND clave = ?",PreparedStatement.RETURN_GENERATED_KEYS);
            PSSELECTOR.setString(1, login);
            PSSELECTOR.setString(2, clave);
            ResultSet rs = PSSELECTOR.executeQuery();
            while (rs.next()) {
                resultado = true;
            }
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
            
        }
        
        return resultado;
    }
    public void gardar() {
        PreparedStatement PSINSERT = null;
        PreparedStatement PSSELECT = null;
        ResultSet rs = null;
        try {
                PSSELECT = Delicious.connection.prepareStatement("SELECT * FROM usuarios WHERE login = ?");
                PSSELECT.setString(1,login);
                rs = PSSELECT.executeQuery();
                
            } catch (SQLException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            if (!rs.next()) {
                
                try {
                    PSINSERT = Delicious.connection.prepareStatement("INSERT INTO usuarios VALUES(?,?)");
                    PSINSERT.setString(1, login);
                    PSINSERT.setString(2, clave);
                    PSINSERT.executeUpdate();

                }catch (SQLException e){
                    e.printStackTrace();

                }
            } else {
                try {
                    PSINSERT = Delicious.connection.prepareStatement("UPDATE usuarios SET login = ?, clave = ? WHERE login = ?");
                    PSINSERT.setString(1, login);
                    PSINSERT.setString(2, clave);
                    PSINSERT.setString(3, rs.getString("login"));
                    PSINSERT.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}



class Enlace{
    private int id, privado;
    private String url, titulo, loginUsuario, comentario;

    public Enlace(int id, int privado, String url, String titulo, String loginUsuario, String comentario) {
        this.id = id;
        this.privado = privado;
        this.url = url;
        this.titulo = titulo;
        this.loginUsuario = loginUsuario;
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginUsuario() {
        return this.loginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }

    public int getPrivado() {
        return privado;
    }

    public void setPrivado(int privado) {
        this.privado = privado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Override
    public String toString() {
        String devolver = this.titulo + " (" + this.url + ") " + this.comentario + "\n";
        try {
            PreparedStatement PSSELECTOR = Delicious.connection.prepareStatement("SELECT * FROM etiquetas WHERE idenlace = ?");
            PSSELECTOR.setInt(1, this.id);
            ResultSet rs = PSSELECTOR.executeQuery();
            if (rs.next()) {
                devolver += rs.getString("etiqueta");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return devolver;
    }
    public static List<Enlace> etiquetados(String etiqueta) {
        ArrayList<Enlace> resultado = new ArrayList<Enlace>();
        try {
            PreparedStatement PSSELECTOR = Delicious.connection.prepareStatement("SELECT * FROM enlaces WHERE id IN (SELECT idenlace FROM etiquetas WHERE etiqueta = ?)",PreparedStatement.RETURN_GENERATED_KEYS);
            PSSELECTOR.setString(1, etiqueta);
            ResultSet rs = PSSELECTOR.executeQuery();
            while (rs.next()) {
                Enlace en = new Enlace(rs.getInt(1), rs.getInt("privado"), rs.getString("url"), rs.getString("titulo"), rs.getString("loginusuario"), rs.getString("comentario"));
                resultado.add(en);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }
        return resultado;
    }
}

public class Delicious extends javax.swing.JFrame {
    static Connection connection;
    /** Creates new form Delicious */
    
    public Delicious() {
        initComponents();
        conectar();
        jMenuItem3.setEnabled(false);
        jLabel4.setText("Usuario Anónimo");
        try {
                PreparedStatement PSSELECT = Delicious.connection.prepareStatement("SELECT e.*, et.etiqueta FROM enlaces e INNER JOIN etiquetas et ON e.id = et.idenlace WHERE privado = 0");
                ResultSet rs = PSSELECT.executeQuery();
                while (rs.next()) {
                    jTextArea1.setText(rs.getString("titulo")+" ("+rs.getString("url")+")\n"+rs.getString("comentario")+ "\n"+rs.getString("etiqueta")+"\n\n");
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jDialog2 = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField5 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel1.setText("Login");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel2.setText("Contraseña");

        jButton1.setText("Registrarse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jButton1)))
                .addContainerGap(116, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addContainerGap(83, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel5.setText("Login");

        jButton2.setText("Logearse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel6.setText("Contraseña");

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                .addGap(76, 76, 76))
            .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog2Layout.createSequentialGroup()
                    .addGap(182, 182, 182)
                    .addComponent(jButton2)
                    .addContainerGap(141, Short.MAX_VALUE)))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(171, Short.MAX_VALUE))
            .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jDialog2Layout.createSequentialGroup()
                    .addGap(194, 194, 194)
                    .addComponent(jButton2)
                    .addContainerGap(83, Short.MAX_VALUE)))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField5.setText("Etiqueta  para filtrar");

        jButton3.setText("Filtrar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jMenu1.setText("Archivo");

        jMenuItem1.setText("Novo Usuario");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Logearse");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Cerrar Sesión");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jButton3))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 692, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel4)
                .addGap(48, 48, 48)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    jMenu1.setEnabled(false);
    jDialog1.setLocation(90,50);
    jDialog1.setSize(340,320);
    jDialog1.setVisible(true);
    jDialog1.setAlwaysOnTop(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    boolean resultado = Usuario.Existe(jTextField1.getText(), jTextField2.getText());
    if (resultado == false) {
        Usuario usuario1 = new Usuario(jTextField1.getText(),jTextField2.getText());
        usuario1.gardar();
        
    }
    jDialog1.setVisible(false);
    jMenu1.setEnabled(true);
    
}//GEN-LAST:event_jButton1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    jMenu1.setEnabled(false);
    jDialog2.setLocation(90,50);
    jDialog2.setSize(340,320);
    jDialog2.setVisible(true);
    jDialog2.setAlwaysOnTop(true);
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    if (Usuario.Existe(jTextField3.getText(), jTextField4.getText())) {
        jLabel4.setText("");
        jLabel4.setText("Bienvenido "+ jTextField3.getText());
        try {
            PreparedStatement PSSELECT = Delicious.connection.prepareStatement("SELECT e.*, et.etiqueta FROM enlaces e INNER JOIN etiquetas et ON e.id = et.idenlace WHERE privado = 1");
            ResultSet rs = PSSELECT.executeQuery();
            while (rs.next()) {
                
                jTextArea1.append(rs.getString("titulo")+" ("+rs.getString("url")+")\n"+rs.getString("comentario")+ "\n"+rs.getString("etiqueta")+"\n\n");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    jDialog2.setVisible(false);
    jMenu1.setEnabled(true);
    jMenuItem1.setEnabled(false);
    jMenuItem2.setEnabled(false);
    jMenuItem3.setEnabled(true);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    List<Enlace> enlaces = new ArrayList<Enlace>();
    /*List<String> etiquetas = new ArrayList<String>();    
    for (String et : jTextField5.getText().split(",")) {
        etiquetas.add(et);
    }*/    
    enlaces = Enlace.etiquetados(jTextField5.getText());
    jTextArea1.setText("");
    for (Enlace e : enlaces) {
        jTextArea1.append(e.toString());
    }
}//GEN-LAST:event_jButton3ActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    jMenuItem1.setEnabled(true);
    jMenuItem2.setEnabled(true);
    jMenuItem3.setEnabled(false);
    jLabel4.setText("");
    jLabel4.setText("Usuario Anónimo");
    jTextArea1.setText("");
    try {
        PreparedStatement PSSELECT = Delicious.connection.prepareStatement("SELECT e.*, et.etiqueta FROM enlaces e INNER JOIN etiquetas et ON e.id = et.idenlace WHERE privado = 0");
        ResultSet rs = PSSELECT.executeQuery();
        while (rs.next()) {
            jTextArea1.setText(rs.getString("titulo")+" ("+rs.getString("url")+")\n"+rs.getString("comentario")+ "\n"+rs.getString("etiqueta")+"\n\n");
        }
        rs.close();
    } catch (SQLException ex) {
        Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
    }
}//GEN-LAST:event_jMenuItem3ActionPerformed
static void conectar() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/delicious","root","qwerty");
            System.out.println("Conexión correcta");
        }catch (SQLException e){
            e.printStackTrace();
            return;
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Delicious.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Delicious.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Delicious.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Delicious.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Delicious().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}
