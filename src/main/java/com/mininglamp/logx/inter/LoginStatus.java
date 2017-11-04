/**
 * Copyright (c) 2016-2026 Mininglamp Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mininglamp.logx.inter;

import java.util.Map;
/**
 * Implement this interface will get the login status from session attributes.
 * @author czy
 * @since 2016年4月25日11:02:50
 */
public interface LoginStatus {
	boolean isLogin(Map<String,Object> sessionAttrs);
}
