    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2008 Peter Welch and Paul Austin.            //
    //                2001-2004 Quickstone Technologies Limited.        //
    //                                                                  //
    //  This library is free software; you can redistribute it and/or   //
    //  modify it under the terms of the GNU Lesser General Public      //
    //  License as published by the Free Software Foundation; either    //
    //  version 2.1 of the License, or (at your option) any later       //
    //  version.                                                        //
    //                                                                  //
    //  This library is distributed in the hope that it will be         //
    //  useful, but WITHOUT ANY WARRANTY; without even the implied      //
    //  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR         //
    //  PURPOSE. See the GNU Lesser General Public License for more     //
    //  details.                                                        //
    //                                                                  //
    //  You should have received a copy of the GNU Lesser General       //
    //  Public License along with this library; if not, write to the    //
    //  Free Software Foundation, Inc., 59 Temple Place, Suite 330,     //
    //  Boston, MA 02111-1307, USA.                                     //
    //                                                                  //
    //  Author contact: P.H.Welch@kent.ac.uk                             //
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.lang;

/**
 * <p>This is an interface to be implemented by classes that wish
 * to act as a client to connect to a <code>ConnectionServer</code>.</p>
 *
 * <p>Users of classes implementing this interface should call
 * <code>request(Object)</code> to initiate a conversation and to send some
 * data to the server.  Implementations may decide to return immediately or
 * to wait until the server accepts the connection and then return. The
 * Connection is not guaranteed to be open until a call to <code>reply()</code>
 * has returned.  The <code>reply()</code> method should be called <I>soon</I>
 * after the call to <code>reqeust(Object)</code>. Some computation may be
 * done between the calls but any external process synchronization is
 * potentially hazardous.</p>
 *
 * <p>After calling <code>reply()</code>, clients can check whether
 * the server closed the connection by calling <code>isOpen()</code>.
 * If it returns <code>true</code>, then the connection has been kept
 * open. If the connection has been kept open then the client may assume
 * that a call to <code>request(Object)</code> will not block and that
 * the connection will <I>soon</I> be dealt with by the server.</p>
 *
 * <p>This is an example of typical code structure for using a
 * ConnectionClient:</p>
 *
 * <pre>
 * //have a variable client of type ConnectionClient
 * do {
 *     client.request(some_data);
 *     some_variable = client.receive();
 * } while (client.isOpen())
 * </pre>
 *
 * @author Quickstone Technologies Limited
 */
public interface ConnectionClient<T>
{
    /**
     * <p>This method is used to send data to a <code>ConnectionServer</code> in
     * a client/server conversation. If a connection has not yet been established,
     * then this method will open the connection as necessary.</p>
     *
     * <p>Once this method has returned, the client may do some computation but
     * must then guarantee to call <code>reply()</code>. This will obtain a
     * server's response to the request. In between calling this method and
     * <code>reply()</code>, doing pure computation is safe. Performing
     * synchronization with other process is potentially hazardous.</p>
     *
     * <p>Once a server replies, if the connection has been kept open, then
     * this method should be called again to make a further request.</p>
     *
     * <p>Programs using <code>Connection</code>s need to adopt a protocol so that the server
     * knows when a conversation with a client has finished and will then drop
     * the connection.</p>
     *
     * @param data	the <code>Object</code> to send to the server.
     * @throws  IllegalStateException	if the method is called when it is
     *                                  not meant to be.
     */
    public void request(T data) throws IllegalStateException;

    /**
     * <p>Receives some data back from the server after
     * <code>request(Object)</code> has been called.</p>
     *
     * <p>After calling this method, <code>isOpen()</code> may be called
     * to establish whether the server dropped the connection after replying.</p>
     *
     * <p>Implementations may make this operation ALTable.</p>
     *
     * @return the <code>Object</code> sent from the server.
     * @throws  IllegalStateException	if the method is called when it is
     *                                  not meant to be.
     */
    public T reply() throws IllegalStateException;

    /**
     * <p>Returns whether the server has kept its end of the Connection open.
     * This should only be called after a call to <code>reply()</code> and
     * before any other Connection method is called.</p>
     *
     * @return <code>true</code> iff the server has kept the connection
     *          open.
     */
    public boolean isOpen() throws IllegalStateException;

}
