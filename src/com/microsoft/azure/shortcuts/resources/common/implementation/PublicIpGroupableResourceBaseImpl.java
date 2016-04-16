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

import com.microsoft.azure.shortcuts.common.implementation.EntitiesImpl;
import com.microsoft.azure.shortcuts.resources.PublicIpAddress;
import com.microsoft.azure.shortcuts.resources.implementation.Subscription;

public abstract class PublicIpGroupableResourceBaseImpl<
		T, 
		I extends com.microsoft.windowsazure.core.ResourceBaseExtended, 
		TI extends PublicIpGroupableResourceBaseImpl<T, I, TI>>
	extends
		GroupableResourceBaseImpl<T, I, TI> {

	protected PublicIpGroupableResourceBaseImpl(String id, I innerObject, EntitiesImpl<Subscription> collection) {
		super(id, innerObject, collection);
	}
	
	protected boolean isPublicIpAddressExisting;
	protected String publicIpAddressId;
	protected String publicIpAddressDns;

	
	// Helper to associate with an existing public IP address using its resource ID
	@SuppressWarnings("unchecked")
	protected TI withExistingPublicIpAddress(String resourceId) {
		this.isPublicIpAddressExisting = true;
		this.publicIpAddressId = resourceId;
		return (TI)this;
	}


	protected final PublicIpAddress ensurePublicIpAddress() throws Exception {
		if(!this.isPublicIpAddressExisting) {
			// Create a new public IP
			if(this.publicIpAddressDns == null) {
				// Generate a public leaf domain name if needed
				this.publicIpAddressDns = this.name().toLowerCase();
			}
			
			PublicIpAddress pip = this.collection.azure().publicIpAddresses().define(this.publicIpAddressDns)
				.withRegion(this.region())
				.withExistingResourceGroup(this.groupName)
				.withLeafDomainLabel(this.publicIpAddressDns)
				.provision();
			this.isPublicIpAddressExisting = true;
			this.publicIpAddressId = pip.id();
			return pip;
		} else if(this.publicIpAddressId != null) {
			return this.collection.azure().publicIpAddresses(this.publicIpAddressId);
		} else {
			return null;
		}
	}

	
	/*****************************************************
	 * WithPublicIpAddress implementation
	 *****************************************************/
	public final TI withExistingPublicIpAddress(com.microsoft.azure.management.network.models.PublicIpAddress publicIpAddress) {
		return this.withExistingPublicIpAddress((publicIpAddress != null) ? publicIpAddress.getId() : null);
	}

	public final TI withExistingPublicIpAddress(PublicIpAddress publicIpAddress) {
		return this.withExistingPublicIpAddress((publicIpAddress != null) ? publicIpAddress.id() : null);
	}

	public final TI withNewPublicIpAddress() {
		return this.withNewPublicIpAddress(null);
	}

	@SuppressWarnings("unchecked")
	public final TI withNewPublicIpAddress(String leafDnsLabel) {
		this.isPublicIpAddressExisting = false;
		this.publicIpAddressDns = (leafDnsLabel == null) ? null : leafDnsLabel.toLowerCase();
		return (TI) this;
	}

	public final TI withoutPublicIpAddress() {
		return this.withExistingPublicIpAddress((PublicIpAddress)null);
	}
}