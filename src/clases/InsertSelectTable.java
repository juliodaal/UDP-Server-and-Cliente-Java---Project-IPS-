package clases;

import static java.lang.Double.parseDouble;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class InsertSelectTable {
    
    //INSERT
    private final String sqlUtilizador = "insert into utilizador (id,nome,pwd,email,tipo_utilizador) values (?,?,?,?,?)";
    private final String sqlCliente = "insert into cliente (id_utilizador) values (?)";
    private final String sqlCartao = "insert into cartao (credito,id_cliente) values (?,?)";
    
    //UPDATE
    private final String sqlcredtioUpdate = "update cartao set credito = ? where id = ?";
    
    
    private PreparedStatement PS;
    private DefaultTableModel DT;
    private ResultSet RS;
    
    private ConnectionBD conn;
    
    public InsertSelectTable(){
        this.PS = null;
        this.conn = new ConnectionBD();
    }
    
    public String insertDataUtilizador(String nome, String pwd, String email,String numeroCartao, int tipo_utilizador){
        try {
            PS = conn.getConnection().prepareStatement(this.sqlUtilizador);
            PS.setString(1, numeroCartao);
            PS.setString(2, nome);
            PS.setString(3, pwd);
            PS.setString(4, email);
            PS.setInt(5, tipo_utilizador);
            int res = PS.executeUpdate();
            if(res > 0){
                return "Registro guardado";
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
           /* JOptionPane.showMessageDialog(null, "Erro ao guardar dados na base de dados : " + "O e-mail já existe");*/
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "" + 0;
    }
    
    public String insertDataCliente(String idUtilizador){
        try {
            PS = conn.getConnection().prepareStatement(this.sqlCliente);
            PS.setString(1, idUtilizador);
            int res = PS.executeUpdate();
            if(res > 0){
                return "Registro guardado";
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "" + 0;
    }
    
    public String insertDataCartao(double credito,int id_cliente){
        try {
            PS = conn.getConnection().prepareStatement(this.sqlCartao);
            PS.setDouble(1, credito);
            PS.setInt(2, id_cliente);
            int res = PS.executeUpdate();
            if(res > 0){
                return "Registro guardado";
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "" + 0;
    }
    
    public String insertDataMensalidade(String email, String mes, String ano){
        int idCliente = getDataIdCliente(getDataIdUtilizador(email));
        try {
            PS = conn.getConnection().prepareStatement("insert into mensalidade (mes,ano, total, id_cliente) values (?,?,29.99,?);");
            PS.setString(1, mes);
            PS.setString(2, ano);
            PS.setInt(3, idCliente);
            int res = PS.executeUpdate();
            if(res > 0){
                return "Pagamento efectuado";
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
           /* JOptionPane.showMessageDialog(null, "Erro ao guardar dados na base de dados : " + "O e-mail já existe");*/
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "" + 0;
    }
    
    public void insertDataTempoTreino(String hora, String data_treino,String entrada, int id_cliente){
        try {
            System.out.println("hora en tabla : " + hora);
            System.out.println("data_traino en tabla : " + data_treino);
            System.out.println("id_cliente en tabla : " + id_cliente);
            PS = conn.getConnection().prepareStatement("insert into tempotreino (hora,data_treino,entrada,id_cliente) values (?,?,?,?)");
            PS.setString(1, hora);
            PS.setString(2, data_treino);
            PS.setString(3, entrada);
            PS.setInt(4, id_cliente);
            int res = PS.executeUpdate();
            if(res > 0){
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
           /* JOptionPane.showMessageDialog(null, "Erro ao guardar dados na base de dados : " + "O e-mail já existe");*/
            //return "Error al guardar los datos en la base de datos : " + e.getMessage();

        } finally {
            PS = null;
        }
    }
    
    //SELECT
    
    public String getDataIdUtilizador(String email){
        String result = "";
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select id from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("id");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public int getDataIdCliente(String idUtilizador){
        int result = 0;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select id from cliente where id_utilizador = ?");
           this.PS.setString(1,idUtilizador);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = Integer.parseInt(this.RS.getString("id"));
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public int getTipoUtilizador(String email){
        int result = 0;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select tipo_utilizador from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = Integer.parseInt(this.RS.getString("tipo_utilizador"));
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getEmail(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select email from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("email");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getPassword(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select pwd from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("pwd");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getName(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select nome from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("nome");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getIdUtilizador(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select id from utilizador where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("id");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getNumeroCartao(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select ca.id from utilizador u join cliente c on u.id = c.id_utilizador join cartao ca on c.id = ca.id_cliente where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("ca.id");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getCreditoCartao(String email){
        String result = null;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select ca.credito from utilizador u join cliente c on u.id = c.id_utilizador join cartao ca on c.id = ca.id_cliente where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getString("ca.credito");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public double getCreditoCartaoWithIdUtilizador(String idUtilizador){
        double result = -1;
        try {
           this.PS = this.conn.getConnection().prepareStatement("select ca.credito from utilizador u join cliente c on u.id = c.id_utilizador join cartao ca on c.id = ca.id_cliente where u.id = ?");
           this.PS.setString(1,idUtilizador);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getDouble("ca.credito");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public int getNumeroCartaoWithIdUtilizador(String idUtilizador){
        int result = -1;
        try {
           this.PS = this.conn.getConnection().prepareStatement("select ca.id from utilizador u join cliente c on u.id = c.id_utilizador join cartao ca on c.id = ca.id_cliente where u.id = ?");
           this.PS.setString(1,idUtilizador);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getInt("ca.id");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getDataTableasPrincipalPage(String email){
        String result = "";
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select * from utilizador u join cliente cli on u.id = cli.id_utilizador join tempotreino t on cli.id = t.id_cliente where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           while(RS.next()){
               result += this.RS.getString("hora") + "#%#" + this.RS.getString("data_treino") + "#%#" + this.RS.getString("entrada") + "#%#";
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getDataTableasMovimentoCartao(String email){
        String result = "";
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select p.nome,data_compra,hora,total from utilizador u join cliente cli on u.id = cli.id_utilizador join cartao c on cli.id = c.id_cliente join pagamentos p on c.id = p.id_cartao where email = ?");
           this.PS.setString(1,email);
           
           this.RS = PS.executeQuery();
           
           while(RS.next()){
               result += this.RS.getString("p.nome") + "#%#" + this.RS.getString("data_compra") + "#%#" + this.RS.getString("hora") + "#%#" + this.RS.getDouble("total") + "#%#";
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        System.out.println("Este es el result : " + result);
        return result;
    }
    
    public String getLastMonth(String email,String currentMonth,int currentYear){
        String result = "";
        try {
            PS = conn.getConnection().prepareStatement("select mes,ano from utilizador u join cliente c on u.id = c.id_utilizador join mensalidade m on c.id = m.id_cliente where mes = ? and ano = ? and email = ?");
            PS.setString(1, currentMonth);
            PS.setInt(2, currentYear);
            PS.setString(3, email);
            
            this.RS = PS.executeQuery();
            System.out.println("RS : " + RS);
            if(RS.next()){
                result = this.RS.getString("mes") + "#%#" + this.RS.getString("ano") + "#%#";
                System.out.println("Este es el result : " + result);
            }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public String getEmailWithIdCliente(String idCliente){
        String result = "";
        try {
            PS = conn.getConnection().prepareStatement("select email from utilizador where id = ?");
            PS.setString(1, idCliente);
            
            this.RS = PS.executeQuery();
            System.out.println("RS : " + RS);
            if(RS.next()){
                result = this.RS.getString("email") + "#%#";
                System.out.println("Este es el result : " + result);
            }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public boolean getConsultMensalidadeCartao(String cartao,String currentMonth,int currentYear){
        boolean result = false;
        try {
            PS = conn.getConnection().prepareStatement("select * from utilizador u join cliente cli on u.id = cli.id_utilizador join mensalidade men on cli.id = men.id_cliente where mes = ? and ano = ?  and u.id = ?");
            PS.setString(1, currentMonth);
            PS.setInt(2, currentYear);
            PS.setString(3, cartao);
            
            this.RS = PS.executeQuery();
            System.out.println("RS : " + RS);
            if(RS.next()){
                result = true;
                System.out.println("Este es el result : " + result);
            }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
            
    public boolean getCompraProduto(String nome,String data,String horas,String preco,int idCartao){
        boolean result = false;
        try {
            PS = conn.getConnection().prepareStatement("insert into pagamentos (nome,data_compra,hora,total,id_cartao) values (?,?,?,?,?)");
            PS.setString(1, nome);
            PS.setString(2, data);
            PS.setString(3, horas);
            PS.setDouble(4, parseDouble(preco));
            PS.setInt(5, idCartao);
            
            int res = PS.executeUpdate();
            if(res > 0){
                result = true;
                System.out.println("Este es el result : " + result);
            }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    // UPDATE
    
    public String upDateCredito(int numeroCartao, double quantidade, String email){
        System.out.println("Este es el get Credito : " + getCreditoCartao(email));
        quantidade += parseDouble(getCreditoCartao(email));
        try {
            PS = conn.getConnection().prepareStatement(this.sqlcredtioUpdate);
            this.PS.setDouble(1,quantidade);
            this.PS.setInt(2,numeroCartao);
            
            int res = PS.executeUpdate();
            if(res > 0){
                return "true";
                //JOptionPane.showMessageDialog(null, "Registro guardado..");
            }
        } catch (SQLException e) {
           /* JOptionPane.showMessageDialog(null, "Erro ao guardar dados na base de dados : " + "O e-mail já existe");*/
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "false";
    }
    
    public String upDateCreditoCompra(String idUtilizador, double quantidade){
  
        int idCartao = getNumeroCartaoWithIdUtilizador(idUtilizador);
             
        try {
            PS = conn.getConnection().prepareStatement("update cartao set credito = ? where id = ?");
            this.PS.setDouble(1,quantidade);
            this.PS.setInt(2,idCartao);
            
            int res = PS.executeUpdate();
            if(res > 0){
                return "true";
            }
        } catch (SQLException e) {
            return "Error al guardar los datos en la base de datos : " + e.getMessage();
        } finally {
            PS = null;
        }
        return "false";
    }
    
    // RFID
    
    public int getCartaoByRFID(int numeroCartao){
        int result = -1;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select id from cartao where id = ?");
           this.PS.setInt(1,numeroCartao);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getInt("id");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
    
    public double getCreditoByRFID(int numeroCartao){
        double result = -1;
        try {
           
           this.PS = this.conn.getConnection().prepareStatement("select credito from cartao where id = ?");
           this.PS.setInt(1,numeroCartao);
           
           this.RS = PS.executeQuery();
           
           if(RS.next()){
               result = this.RS.getDouble("credito");
           }
           
        } catch(SQLException e){
            System.out.println("Error al listar los datos : " + e.getMessage());
        } finally{
            PS = null;
            RS = null;
        }
        return result;
    }
}
