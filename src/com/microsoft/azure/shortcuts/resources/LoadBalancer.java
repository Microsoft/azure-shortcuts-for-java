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
package com.microsoft.azure.shortcuts.resources;

import com.microsoft.azure.shortcuts.common.Deletable;
import com.microsoft.azure.shortcuts.common.Provisionable;
import com.microsoft.azure.shortcuts.common.Refreshable;
import com.microsoft.azure.shortcuts.common.Wrapper;
import com.microsoft.azure.shortcuts.resources.common.DefinitionCombos;
import com.microsoft.azure.shortcuts.resources.common.GroupResourceBase;

public interface LoadBalancer extends 
	GroupResourceBase,
	Refreshable<LoadBalancer>,
	Wrapper<com.microsoft.azure.management.network.models.LoadBalancer>,
	Deletable {
	
	/**
	 * A new blank load balancer definition
	 */
	interface DefinitionBlank extends
		GroupResourceBase.DefinitionWithRegion<DefinitionWithGroup> { }
	
	interface DefinitionWithGroup extends
		GroupResourceBase.DefinitionWithResourceGroup<DefinitionWithFrontEnd> {}
	
	/**
	 * A load balancer definition allowing to specify a front end IP address
	 */
	interface DefinitionWithFrontEnd extends
		DefinitionCombos.WithPublicIpAddress<DefinitionProvisionable> {
	}
	
	/**
	 * A load balancer definition with sufficient input parameters specified to be provisioned in the cloud
	 */

	interface DefinitionProvisionable extends
		Provisionable<LoadBalancer>,
		GroupResourceBase.DefinitionWithTags<DefinitionProvisionable> {
	}
}
