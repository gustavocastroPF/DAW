package br.edu.ifsul.util;

/**
 *
 * @author gustavo
 */
public class Util {
    
    public static String getMensagemErro(Exception e){
        while(e.getCause()!=null){
            e = (Exception) e.getCause();
        }
        String retorno = e.getMessage();
        if(retorno.contains("foreign key")){
            retorno ="Regitro não pode ser excluido, pois possui referencia em outros objetos";
        }
        return retorno;
    }
    
    
}
