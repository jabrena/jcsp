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
 * <p>
 * This class is sub-classed by JCSP.NET classes
 * to provide <code>ConnectionClient</code> objects which can
 * have their <code>receive()</code> method alted over.
 * </p>
 * <p>
 * Although JCSP users could sub-class this class, under most
 * circumstances, there is no need. <code>AltingConnectionClient</code>
 * objects can be constructed using one of the Connection factory
 * mechanisms. See <code>{@link Connection}</code> and
 * <code>{@link StandardConnectionFactory}</code>.
 * </p>
 *
 * @author Quickstone Technologies Limited
 */
public abstract class AltingConnectionClient<T> extends Guard implements ConnectionClient<T>
{
    /**
     * The channel used to ALT over.
     */
    private AltingChannelInput<T> altingChannel;

    /**
     * <p>
     * Constructor.
     * </p>
     * <p>
     * Note that this is only intended for use by JCSP, and should
     * not be called by user processes.  Users should use one of the
     * subclasses.
     * </p>
     * @param altingChannel The channel used to implement the Guard
     */
    protected AltingConnectionClient(AltingChannelInput<T> altingChannel)
    {
        this.altingChannel = altingChannel;
    }

    /**
     * <p>
     * Returns the channel used to implement the Guard.
     * </p>
     * <p>
     * Note that this method is only intended for use by
     * JCSP, and should not be called by user processes.
     * </p>
     * <p>
     * Concrete subclasses should override this method to
     * return null, to ensure that the alting channel is
     * kept private.
     * </p>
     * @return The channel passed to the constructor.
     */
    protected AltingChannelInput<T> getAltingChannel()
    {
        return this.altingChannel;
    }

    /**
     * <p>
     * <code>ConnectionServer</code> implementations are likely to be
     * implemented over channels. Multiple channels from the client
     * to server may be used; one could be used for the initial
     * connection while another one could be used for data requests.
     * </p>
     * <p>
     * This method allows sub-classes to specify which channel should
     * be the next one to be alted over.
     * </p>
     *
     * @param	chan	the channel to be ALTed over.
     */
    protected void setAltingChannel(AltingChannelInput<T> chan)
    {
        this.altingChannel = chan;
    }

    /**
     * <p>
     * Returns true if the event is ready.  Otherwise, this enables the guard
     * for selection and returns false.
     * </p>
     * <p>
     * <I>Note: this method should only be called by the Alternative class</I>
     * </p>
     * @param alt the Alternative class that is controlling the selection
     * @return true if and only if the event is ready
     */
    boolean enable(Alternative alt)
    {
        return altingChannel.enable(alt);
    }

    /**
     * <p>
     * Disables the guard for selection. Returns true if the event was ready.
     * </p>
     * <p>
     * <I>Note: this method should only be called by the Alternative class</I>
     * </p>
     * @return true if and only if the event was ready
     */
    boolean disable()
    {
        return altingChannel.disable();
    }

    /**
     * <p>
     * Returns whether there is an open() pending on this connection. <p>
     * </p>
     * <p>
     * <i>Note: if there is, it won't go away until you accept it.  But if
     * there isn't, there may be one by the time you check the result of
     * this method.</i>
     * </p>
     * @return true only if open() will complete without blocking.
     */
    public boolean pending()
    {
        return altingChannel.pending();
    }
}
