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

import com.microsoft.azure.shortcuts.resources.Group;
import com.microsoft.azure.shortcuts.resources.common.GroupResourceBase;
import com.microsoft.azure.shortcuts.resources.implementation.Azure;
import com.microsoft.azure.shortcuts.resources.implementation.ResourcesImpl;


public abstract class GroupResourceBaseImpl<T, I extends com.microsoft.windowsazure.core.ResourceBaseExtended>
	extends
		ResourceBaseImpl<T, I>
	implements 
		GroupResourceBase {

	protected GroupResourceBaseImpl(String id, I innerObject) {
		super(id, innerObject);
	}

	protected String groupName;
	protected boolean isExistingGroup;
	
	/*******************************************
	 * Getters
	 *******************************************/
	
	@Override 
	public String group() {
		return ResourcesImpl.groupFromResourceId(this.id());
	}
	
	/**************************************************
	 * Helpers
	 * @throws Exception 
	 **************************************************/
	protected Group ensureGroup(Azure azure) throws Exception {
		Group group;
		if(!this.isExistingGroup) {
			if(this.groupName == null) {
				this.groupName = "group" + this.name();
			}
				
			group = azure.groups().define(this.groupName)
					.withRegion(this.region())
					.provision();
			this.isExistingGroup = true;
			return group;
		} else {
			return azure.groups(this.groupName);
		}
	}
	
	
	protected GroupResourceBaseImpl<T, I> withGroupExisting(String groupName) {
		this.groupName = groupName;
		this.isExistingGroup = true;
		return this;
	}
	
	protected GroupResourceBaseImpl<T, I> withGroupNew(String groupName) {
		this.groupName = groupName;
		this.isExistingGroup = false;
		return this;
	}
}