/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package CodigosDeTestes;

import javax.sip.*;

/**
 *
 * @author eriko
 */
public interface IOuvindoSIP {
    
    void processRequest(RequestEvent evt);
    void processResponse(ResponseEvent evt);
    void processTimeout(TimeoutEvent evt);
    void processIOException(IOExceptionEvent evt);
    void processTransactionTerminated(TransactionTerminatedEvent evt);
    void processDialogTerminated(DialogTerminatedEvent evt);


}
