/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.managers.proxy;

/**
 * @author Mikle Garin
 */

public class WebProxyManager
{
    /**
     * Whether manager is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes manager if it wasn't already initialized.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // Default proxy authentificator
            ProxyManager.setAuthenticator ( new WebProxyAuthenticator () );
            ProxyManager.setSystemProxyConfirmationSupport ( new WebSystemProxyConfirmationSupport () );

            // Ensure ProxyManager is initialized
            ProxyManager.initialize ();
        }
    }
}