/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.authentication;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link AuthenticationObservationConvention}
 */
public class AuthenticationObservationConventionTests {

	private final AuthenticationObservationConvention convention = new AuthenticationObservationConvention();

	@Test
	void getContextualNameWhenNoAuthenticationRequestThenDefault() {
		AuthenticationObservationContext context = new AuthenticationObservationContext();
		assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate");
	}

	@Test
	void getContextualNameWhenAuthenticationRequestThenStripsTokenSuffix() {
		AuthenticationObservationContext context = new AuthenticationObservationContext();
		context.setAuthenticationRequest(new UsernamePasswordAuthenticationToken("user", "password"));
		assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate usernamepassword");
	}

	@Test
	void getContextualNameWhenTurkishDefaultLocaleThenLocaleIndependent() {
		Locale defaultLocale = Locale.getDefault();
		try {
			Locale.setDefault(Locale.forLanguageTag("tr"));
			AuthenticationObservationContext context = new AuthenticationObservationContext();
			context.setAuthenticationRequest(new InternalAuthenticationToken());
			assertThat(this.convention.getContextualName(context)).isEqualTo("authenticate internal");
		}
		finally {
			Locale.setDefault(defaultLocale);
		}
	}

	static final class InternalAuthenticationToken extends AbstractAuthenticationToken {

		InternalAuthenticationToken() {
			super(AuthorityUtils.NO_AUTHORITIES);
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return "user";
		}

	}

}
