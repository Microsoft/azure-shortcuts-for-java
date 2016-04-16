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

import com.microsoft.azure.management.resources.models.ResourceGroupExtended;
import com.microsoft.azure.shortcuts.common.implementation.EntitiesImpl;
import com.microsoft.azure.shortcuts.resources.ResourceGroup;
import com.microsoft.azure.shortcuts.resources.common.GroupResourceBase;
import com.microsoft.azure.shortcuts.resources.implementation.Subscription;
import com.microsoft.azure.shortcuts.resources.implementation.ResourcesImpl;


public abstract class GroupableResourceBaseImpl<
		WRAPPER, 
		INNER extends com.microsoft.windowsazure.core.ResourceBaseExtended, 
		WRAPPERIMPL extends GroupableResourceBaseImpl<WRAPPER, INNER, WRAPPERIMPL>>
	extends
		ResourceBaseImpl<WRAPPER, INNER, WRAPPERIMPL>
	implements 
		GroupResourceBase {

	protected GroupableResourceBaseImpl(String id, INNER innerObject, EntitiesImpl<Subscription> collection) {
		super(id, innerObject, collection);
	}

	protected String groupName;
	protected boolean isExistingGroup;
	
	/*******************************************
	 * Getters
	 *******************************************/
	
	@Override
	public final String resourceGroup() {
		String groupNameTemp = ResourcesImpl.groupFromResourceId(this.id());
		return (groupNameTemp == null) ? this.groupName : groupNameTemp;
	}
	
	
	/**************************************************
	 * Helpers
	 * @throws Exception 
	 **************************************************/
	protected final ResourceGroup ensureGroup() throws Exception {
		ResourceGroup group;
		if(!this.isExistingGroup) {
			if(this.groupName == null) {
				this.groupName = this.name() + "group";
			}
			
			group = this.collection.azure().resourceGroups().define(this.groupName)
				.withRegion(this.region())
				.provision();
			this.isExistingGroup = true;
			return group;
		} else {
			return this.collection.azure().resourceGroups(this.groupName);
		}
	}
	
	
	/****************************************
	 * withGroup implementations
	 ****************************************/
	
	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withExistingResourceGroup(String groupName) {
		this.groupName = groupName;
		this.isExistingGroup = true;
		return (WRAPPERIMPL)this;
	}
	
	
	@SuppressWarnings("unchecked")
	public final WRAPPERIMPL withNewResourceGroup(String groupName) {
		this.groupName = groupName;
		this.isExistingGroup = false;
		return (WRAPPERIMPL) this;
	}
	
	public final WRAPPERIMPL withNewResourceGroup() {
		return this.withNewResourceGroup((String)null);
	}
	
	public final WRAPPERIMPL withNewResourceGroup(ResourceGroup.DefinitionProvisionable groupDefinition) throws Exception {
		return withExistingResourceGroup(groupDefinition.provision());
	}
	
	public final WRAPPERIMPL withExistingResourceGroup(ResourceGroup group) {
		return this.withExistingResourceGroup(group.name());
	}
	
	public final WRAPPERIMPL withExistingResourceGroup(ResourceGroupExtended group) {
		return this.withExistingResourceGroup(group.getName());
	}
}