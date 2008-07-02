/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Interfaces;

/**
 *
 * @author eriko
 */
public interface IProcessadorMensagem {
   
    public void processarMensagem(String remetente, String mensagem);
    public void processarErro(String mensagemErro);
    public void processarInfo(String mensagemInfo);

}
