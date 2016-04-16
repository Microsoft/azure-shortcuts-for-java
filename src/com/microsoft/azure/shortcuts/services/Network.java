/**
* Copyright (c) Microsoft Corporation
* 
* All rights reserved. 
* 
* MIT License
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files 
* (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, 
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
* subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
* ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH 
* THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package com.microsoft.azure.shortcuts.services;

import java.util.List;
import java.util.Map;

import com.microsoft.azure.shortcuts.common.Deletable;
import com.microsoft.azure.shortcuts.common.Indexable;
import com.microsoft.azure.shortcuts.common.Provisionable;
import com.microsoft.azure.shortcuts.common.Refreshable;
import com.microsoft.azure.shortcuts.common.Updatable;
import com.microsoft.azure.shortcuts.common.Wrapper;
import com.microsoft.windowsazure.management.network.models.NetworkListResponse.VirtualNetworkSite;

public interface Network extends 
	Indexable,
	Refreshable<Network>,
	Wrapper<VirtualNetworkSite> {
	
	List<String> addressPrefixes() throws Exception;
	String region() throws Exception;
	String affinityGroup() throws Exception;
	String label() throws Exception;
	Map<String, Subnet> subnets() throws Exception;
	String state() throws Exception;
	
	interface Subnet extends Indexable {
		String addressPrefix();
		String networkSecurityGroup();
	}

	/**
	 * A new blank network definition
	 */
	interface DefinitionBlank extends
		DefinitionWithRegion<DefinitionWithAddressSpace> {
	}
	
	/**
	 * A virtual network definition requiring a region to be specified
	 */
	interface DefinitionWithRegion<T> {
		T withRegion(String region);
		T withRegion(Region region);		
	}
	
	/**
	 * A virtual network definition requiring a new subnet to be specified
	 */
	interface DefinitionWithSubnet<T> {
		T withSubnet(String name, String cidr);		
	}
	
	/**
	 * A new network definition with sufficient input parameters specified to be provisioned in the cloud
	 */
	interface DefinitionProvisionable extends
		DefinitionWithSubnet<DefinitionProvisionable>,
		Provisionable<UpdateBlank> {
	}
	
	/**
	 * A new network definition requiring the CIDR input parameter to be specified
	 */
	interface DefinitionWithAddressSpace {
		DefinitionProvisionable withAddressSpace(String cidr);
	}
	
	
	/**
	 * A blank update request for an existing network
	 */
	interface UpdateBlank extends Deletable {
		// TODO?
	}
	
	
	/**
	 * An existing network update request ready to be applied in the cloud
	 */
	interface Update extends UpdateBlank, Updatable<Update> {
	}

}
