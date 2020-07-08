package clases;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class UdpServer {
    
    private boolean value;
    private InsertSelectTable table; 
    private String guardado;
    private boolean entrada;
    
    public UdpServer(){
        this.value = false;
        this.table = new InsertSelectTable();
        
        this.entrada = true;
    }
    
    public void setValue(Boolean value){
        this.value = value;
    }
    
    public Boolean isValue(){
        return this.value;
    }
    
    public void initServer() {
        final int PORT = 50000;
        byte[] buffer = new byte[1024];
        try {
            
                System.out.println("Iniciando el Servidor UDP");
                DatagramSocket socketUDP = new DatagramSocket(PORT);
                
            while(isValue()){
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
                
                socketUDP.receive(peticion);
                
                String mensaje = new String(peticion.getData());
                
                System.out.println(mensaje);
                
                // Manejo de peticion
                System.out.println("Este es el mensaje que llega al servidor : " + mensaje);
                
                System.out.println("Este es el mensaje desencriptado : " + desencriptar(mensaje));
                
                // distributor(desencriptar(mensaje));
                distributor(mensaje);
                // Fin de manejo de peticion
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                String mensaje2 = new String(this.guardado);
                System.out.println("Este es el tamano del msg: " + mensaje2.length());
                System.out.println("Este es mensaje en el Servidor: " + mensaje2);
                
                byte[] buffer2 = new byte[1024];
                //buffer2 = encriptar(mensaje2).getBytes();
                buffer2 = mensaje2.getBytes();
                System.out.println("Este es el tamaño buffer : " + buffer2.length);
                DatagramPacket respuesta = new DatagramPacket(buffer2, buffer2.length, direccion, puertoCliente);

                socketUDP.send(respuesta);
            }
            
            if(isValue() == false){
                socketUDP.close();
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(clases.UdpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clases.UdpServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UdpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String desencriptar(String textoEncriptado) throws Exception {

        String secretKey = "qualityinfosolutions"; //llave para desenciptar datos
        String base64EncryptedString = "";

        try {
            byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");

            Cipher decipher = Cipher.getInstance("DESede");
            decipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = decipher.doFinal(message);

            base64EncryptedString = new String(plainText, "UTF-8");

        } catch (Exception ex) {
        }
        return base64EncryptedString;
}
    
    public static String encriptar(String texto) {

            String secretKey = "qualityinfosolutions"; //llave para encriptar datos
            String base64EncryptedString = "";

            try {

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
                byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

                SecretKey key = new SecretKeySpec(keyBytes, "DESede");
                Cipher cipher = Cipher.getInstance("DESede");
                cipher.init(Cipher.ENCRYPT_MODE, key);

                byte[] plainTextBytes = texto.getBytes("utf-8");
                byte[] buf = cipher.doFinal(plainTextBytes);
                byte[] base64Bytes = Base64.encodeBase64(buf);
                base64EncryptedString = new String(base64Bytes);

            } catch (Exception ex) {
            }
            return base64EncryptedString;
        }
    
    public void distributor(String mensaje){
        String accum = "", accum2 = "";
        String nameTable = "";
        for(int i = 0; i < mensaje.length(); i++){
            char c = mensaje.charAt(i); 
            if(c == '#' || c == '%'){
                accum2 += c;
                accum += c;
                if("#%#".equals(accum2)){
                    nameTable = deleteWord(accum,accum2);
                    break;
                }
            } else {
                accum += c; 
            }
        }
       
        switch(nameTable){
            case "signin":
                    signIn(mensaje);
                break;
            case "login":
                    login(mensaje);
                break;
            case "principalpage":
                    principalpage(mensaje);
                break;
            case "recarregarcartao":
                    recarregarcartao(mensaje);
                break;
            case "verificacaoentrada":
                   /* verificacaoentrada(mensaje);*/
                break;
            case "tableprincipalpage":
                    tableprincipalpage(mensaje);
                break;
            case "tablemovimentocartao":
                    tablemovimentocartao(mensaje);
                break;
            case "verificarmensalidade":
                    verificarmensalidade(mensaje);
                break;
            case "pagarmensalidade":
                    pagarmensalidade(mensaje);
                break;
            case "cancela":
                    cancela(mensaje);
                break;
            case "catchidutilizador":
                    catchidutilizador(mensaje);
                break;
            case "cancelaPagamentoProduto":
                    cancelaPagamentoProduto(mensaje);
                break;
            case "procurarcliente":
                    procurarcliente(mensaje);
                break;
        }
        
    }
    
    public static String  deleteWord(String sentence,String word) {
        if(sentence.contains(word))
            return sentence.replaceAll(word, "");
        return sentence;
    }
    
    public void signIn(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String nome = "", pwd = "", email = "", numeroCartao = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                nome = word;
                                break;
                            case 3:
                                pwd = word;
                                break;
                            case 4:
                                email = word;
                                break;
                            case 5: 
                                numeroCartao = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("nome : " + nome);
            System.out.println("pass : " + pwd);
            System.out.println("email : " + email);
            System.out.println("Numero Cartão : " + numeroCartao);
            this.guardado = "";
            this.guardado = this.table.insertDataUtilizador(nome,pwd,email,numeroCartao,1);
            this.table.insertDataCliente(numeroCartao);
            this.table.insertDataCartao(0,this.table.getDataIdCliente(numeroCartao));
    }
    
    public void login(String mensaje){
        System.out.println("Ver mensaje en login");
        System.out.println(mensaje);
        String accum = "",accum2 = "", word = "", nameTable = "";
        String pwd = "", email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            case 3:
                                pwd = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            System.out.println("pass : " + pwd);
            
            if(this.table.getEmail(email) != null){
            if(this.table.getPassword(email).equals(pwd)){
                this.guardado = "";
                this.guardado = "true" + "#%#" + this.table.getTipoUtilizador(email) + "#%#";
                System.out.println("this.guardado: " + this.guardado);
            } else {
                this.guardado = "";
                this.guardado = "false";
                System.out.println("this.guardado: " + this.guardado);
            }
        } else {
            this.guardado = "";
            this.guardado = "false";
            System.out.println("this.guardado: " + this.guardado);
        }
    }
    
    public void principalpage(String mensaje){
        String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            this.guardado = "";
            this.guardado = this.table.getName(email) + "#%#" + this.table.getNumeroCartao(email) + "#%#" + this.table.getCreditoCartao(email) + "#%#";
    }
    
    public void recarregarcartao(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String credito = "", cartao = "", email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                cartao = word;
                                break;
                            case 3:
                                credito = word;
                                break;
                            case 4:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("cartao : " + cartao);
            System.out.println("credito : " + credito);
            System.out.println("email : " + credito);
            this.guardado = "";
            this.guardado = this.table.upDateCredito(parseInt(cartao), parseDouble(credito), email);
    }
    
    public void tableprincipalpage(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            this.guardado = "";
            
            System.out.println("Ver return: " + this.table.getDataTableasPrincipalPage(email));
            this.guardado = this.table.getDataTableasPrincipalPage(email);
    }
    
    public void tablemovimentocartao(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            this.guardado = "";
            this.guardado = this.table.getDataTableasMovimentoCartao(email);
    }
    public void verificarmensalidade(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            
            LocalDate currentdate = LocalDate.now();
            Month currentMonth = currentdate.getMonth();
            int currentYear = currentdate.getYear();
            
            String msg;            
            String accumt = "",accum22 = "", word2 = "";
            String mes = "", ano = "";
            int cont2 = 1;
            String lastMonth = "";
            String mesSend = String.valueOf(currentMonth);
            
            do{
                System.out.println("Este es el email : " + email);
                System.out.println("Este es el mesSend : " + mesSend);
                System.out.println("Este es el currentYear : " + currentYear);
                
                msg =  this.table.getLastMonth(email,mesSend,currentYear);    
                
                for(int i = 0; i < msg.length(); i++){
                char c2 = msg.charAt(i); 
                    if(c2 == '#' || c2 == '%'){
                        accum22 += c2;
                        accumt += c2;
                        if("#%#".equals(accum22)){
                            word2 = deleteWord(accumt,accum22);
                            System.out.println("Ver accum:------");
                            System.out.println(word2);
                            switch(cont2){
                            case 1:
                                mes = "";
                                mes = word2;
                                break;
                            case 2:
                                ano = "";
                                ano = word2;
                                break;
                            }
                            accum22 = "";
                            accumt = "";
                            cont2++;
                        }
                    } else {
                        accumt += c2;
                    }
                }
                cont2 = 1;
                
                if("DECEMBER".equals(mes)){
                    currentYear = parseInt(ano) + 1;
                }
                if("".equals(mes)){
                    System.out.println("Entro en if(''.equals(mes))");
                    if("".equals(lastMonth)){
                        System.out.println("Entro en if(''.equals(lastMonth))");
                        System.out.println("Valor mesSend : " + mesSend);
                        ano = String.valueOf(currentYear);
                    } else {
                        mesSend = this.nextMonth(lastMonth);
                    }
                } else {
                    System.out.println("Ver mes : " + mes);
                    System.out.println("Ver mes : " + ano);
                    mesSend = this.nextMonth(mes);
                    lastMonth = mesSend;
                    System.out.println("Ver lastMonth : " + lastMonth);
                }
            }while(!("".equals(msg)));
            
            
            System.out.println("mesSend : " + mesSend);
            this.guardado = "";
            System.out.println("Este es el msg : " + msg);
            if(!("".equals(msg))){
                this.guardado = currentMonth + "#%#" + currentYear + "#%#";
            } else {
                this.guardado = mesSend + "#%#" + ano + "#%#";
            }
            
            System.out.println("Este es this.guardado : " + this.guardado);
    }
    public String nextMonth(String mes){
        String month = "NULL";
        switch(mes){
            case "JANUARY":
                    month = "FEBRUARY";
                break;
            case "FEBRUARY":
                    month = "MARCH";
                break;
            case "MARCH":
                    month = "APRIL";
                break;
            case "APRIL":
                    month = "MAY";
                break;    
            case "MAY":
                    month = "JUNE";
                break;
            case "JUNE":
                    month = "JULY";
                break;
            case "JULY":
                    month = "AUGUST";
                break;
            case "AUGUST":
                    month = "SEPTEMBER";
                break;
            case "SEPTEMBER":
                    month = "OCTOBER";
                break;
            case "OCTOBER":
                    month = "NOVEMBER";
                break;
            case "NOVEMBER":
                    month = "DECEMBER";
                break;
            case "DECEMBER":
                    month = "JANUARY";
                break;
                
        }
        return month;
    }
    
    public void pagarmensalidade(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "", mes = "", ano = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            case 3:
                                mes = word;
                                break;
                            case 4:
                                ano = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            System.out.println("mes : " + mes);
            System.out.println("ano : " + ano);
            this.guardado = "";
            this.guardado = this.table.insertDataMensalidade(email,mes,ano);
    }        
    
    public void cancela(String mensaje){
        
        String accum = "",accum2 = "", word = "", nameTable = "";
        String cartao = "", ent = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver word:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                cartao = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("cartao : " + cartao);
            
            LocalDate currentdate = LocalDate.now();
            Month currentMonth = currentdate.getMonth();
            int currentYear = currentdate.getYear();
            int currentDay = currentdate.getDayOfMonth();
            
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = dateFormat.format(date);
            
            this.guardado = "" + this.table.getConsultMensalidadeCartao(cartao,String.valueOf(currentMonth),currentYear);
            
            if(this.entrada == true){
                ent = "entrada";
            } else {
                ent = "saida";
            }
            this.entrada =  !this.entrada;
            
            if("true".equals(this.guardado)){
                int currentMonthNumber = currentdate.getMonthValue();
                System.out.println("Hora : " + formattedDate);
                System.out.println("Fecha : " + currentYear+"-"+currentMonthNumber+"-"+currentDay);
                System.out.println("Id Cliente : " + this.table.getDataIdCliente(cartao));
                int id = this.table.getDataIdCliente(cartao);
                this.table.insertDataTempoTreino(String.valueOf(formattedDate),String.valueOf(currentYear+"-"+currentMonthNumber+"-"+currentDay),ent,id);
            }
    }
    
    public void catchidutilizador(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String email = "", mes = "", ano = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                email = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("email : " + email);
            this.guardado = "";
            this.guardado = this.table.getIdUtilizador(email) + "#%#";
    } 
            
    public void cancelaPagamentoProduto(String mensaje){
        
        String accum = "",accum2 = "", word = "", nameTable = "";
        String idUtilizador = "", preco = "", nome = "" , name ;
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver word:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                idUtilizador = word;
                                break;
                            case 3:
                                preco = word;
                                break;
                            case 4:
                                nome = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("idUtilizador : " + idUtilizador);
            System.out.println("nome : " + nome);
            System.out.println("preco : " + preco);
            
            double creditoCartao = this.table.getCreditoCartaoWithIdUtilizador(idUtilizador);
            boolean tipoEnvio = false;
            if(creditoCartao >= parseDouble(preco)){  
                creditoCartao -= parseDouble(preco);  
                this.table.upDateCreditoCompra(idUtilizador, creditoCartao);
                tipoEnvio = true;
            }
            
            LocalDate currentdate = LocalDate.now();
            Month currentMonth = currentdate.getMonth();
            int currentYear = currentdate.getYear();
            int currentDay = currentdate.getDayOfMonth();
            
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = dateFormat.format(date);
            
            int currentMonthNumber = currentdate.getMonthValue();
            
            this.guardado = "";
            int idCartao =  this.table.getNumeroCartaoWithIdUtilizador(idUtilizador);
            if(idCartao == -1){
                System.out.println("Id utilizador no valido");
            } 
            
            if(tipoEnvio == true){  
                this.guardado = "" + this.table.getCompraProduto(nome,String.valueOf(currentYear+"-"+currentMonthNumber+"-"+currentDay),String.valueOf(formattedDate),preco,idCartao) + "P";
            } else {
                this.guardado = "falseP";
            } 
    }
    
    public void procurarcliente(String mensaje){
    String accum = "",accum2 = "", word = "", nameTable = "";
        String idCliente = "";
        int cont = 1;
            for(int i = 0; i < mensaje.length(); i++){
                char c = mensaje.charAt(i); 
                    if(c == '#' || c == '%'){
                        accum2 += c;
                        accum += c;
                        if("#%#".equals(accum2)){
                            word = deleteWord(accum,accum2);
                            System.out.println("Ver accum:------");
                            System.out.println(word);
                            switch(cont){
                            case 1:
                                nameTable = word;
                                break;
                            case 2:
                                idCliente = word;
                                break;
                            }
                            accum2 = "";
                            accum = "";
                            cont++;
                        }
                    } else {
                        accum += c;
                    }
            }
            System.out.println("idCliente : " + idCliente);
            this.guardado = "";
            this.guardado = this.table.getEmailWithIdCliente(idCliente);
    }
}
