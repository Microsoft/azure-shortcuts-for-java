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
package com.microsoft.azure.shortcuts.resources.common.implementation;

import com.microsoft.azure.management.network.models.VirtualNetwork;
import com.microsoft.azure.shortcuts.common.implementation.EntitiesImpl;
import com.microsoft.azure.shortcuts.resources.Network;
import com.microsoft.azure.shortcuts.resources.implementation.Subscription;

public abstract class NetworkableGroupableResourceBaseImpl<
		WRAPPER, 
		INNER extends com.microsoft.windowsazure.core.ResourceBaseExtended, 
		WRAPPERIMPL extends NetworkableGroupableResourceBaseImpl<WRAPPER, INNER, WRAPPERIMPL>>
	extends
		PublicIpGroupableResourceBaseImpl<WRAPPER, INNER, WRAPPERIMPL> {

	protected NetworkableGroupableResourceBaseImpl(String id, INNER innerObject, EntitiesImpl<Subscription> collection) {
		super(id, innerObject, collection);
	}
	
	private boolean isNetworkExisting;
	private String networkId;
	private String networkCidr;
	private String subnetId;
	protected String privateIpAddress;


	protected final Network ensureNetwork() throws Exception {
		if(!this.isNetworkExisting) {
			// Create a new virtual network
			if(this.networkId == null) {
				// Generate a name if needed
				this.networkId = this.name() + "net";
			}
	
			Network network = this.collection.azure().networks().define(this.networkId)
				.withRegion(this.region())
				.withExistingResourceGroup(groupName)
				.withAddressSpace(this.networkCidr)
				.provision();
			this.isNetworkExisting = true;
			return network;
		} else {
			return this.collection.azure().networks(this.networkId);
		}
	}


	protected final Network.Subnet ensureSubnet(Network network) throws Exception {
		if(network == null) {
			return null;
		} else if(this.subnetId != null) {
			return network.subnets(this.subnetId);
		} else {
			// If no subnet specified, return the first one
			return network.subnets().values().iterator().next();
		}
	}
	
	
	/***********************************************************
	 * WithNetwork* Implementation
	 ***********************************************************/
	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withExistingNetwork(String id) {
		this.isNetworkExisting = true;
		this.networkId = id;
		return (WRAPPERIMPL)this;
	}

	public final WRAPPERIMPL withExistingNetwork(Network network) {
		return this.withExistingNetwork(network.id());
	}

	public final WRAPPERIMPL withExistingNetwork(VirtualNetwork network) {
		return this.withExistingNetwork(network.getId());
	}

	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withNewNetwork(String name, String addressSpace) {
		this.isNetworkExisting = false;
		this.networkId = name;
		this.networkCidr = addressSpace;
		return (WRAPPERIMPL) this;
	}

	public final WRAPPERIMPL withNewNetwork(Network.DefinitionProvisionable networkDefinition) throws Exception {
		return this.withExistingNetwork(networkDefinition.provision());
	}

	public final WRAPPERIMPL withNewNetwork(String addressSpace) {
		return this.withNewNetwork((String)null, addressSpace);
	}
	
	
	/********************************************************
	 * WithSubnet implementation
	 ********************************************************/
	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withSubnet(String subnetId) {
		this.subnetId = subnetId;
		return (WRAPPERIMPL)this;
	}
	

	/*******************************************************
	 * WithPrivateIpAddress implementation
	 *******************************************************/
	public final WRAPPERIMPL withPrivateIpAddressDynamic() {
		return this.withPrivateIpAddressStatic(null);
	}
	
	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withPrivateIpAddressStatic(String staticPrivateIpAddress) {
		this.privateIpAddress = staticPrivateIpAddress;
		return (WRAPPERIMPL)this;
	}
}