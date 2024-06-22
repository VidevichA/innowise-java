import { ReactKeycloakProvider } from '@react-keycloak/web';
import Keycloak from 'keycloak-js';
import ReactDOM from 'react-dom/client';
import { App } from './App';

const keycloak = new Keycloak({
	clientId: 'react-app',
	realm: 'realm',
	url: 'http://localhost:9080',
});

ReactDOM.createRoot(document.getElementById('root')!).render(
	<ReactKeycloakProvider
		authClient={keycloak}
		initOptions={{ onLoad: 'login-required' }}
	>
		<App />
	</ReactKeycloakProvider>
);
