import { useKeycloak } from '@react-keycloak/web';

export const App = () => {
	const { keycloak, initialized } = useKeycloak();

	if (!initialized) {
		return <div>Loading...</div>;
	}

	function sendRequst(): void {
		// axios.post('http://localhost:9080/api/v1/protected', {})
		//     .then(function (response) {
		//         console.log(response);
		//     })
		//     .catch(function (error) {
		//         console.log(error);
		//     });
		console.log(keycloak.token);
	}

	return (
		<div>
			<div>{`User is ${
				!keycloak.authenticated ? 'NOT ' : ''
			}authenticated`}</div>

			{!!keycloak.authenticated && (
				<>
					<button type='button' onClick={() => sendRequst()}>
						send
					</button>
					<button type='button' onClick={() => keycloak.logout()}>
						Logout
					</button>
				</>
			)}
		</div>
	);
};
