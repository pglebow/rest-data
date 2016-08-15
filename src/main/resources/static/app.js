'use strict';

// tag::vars[]
const React = require('react');
const client = require('./client');
// end::vars[]

// tag::app[]
class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {users: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/users'}).done(response => {
			this.setState({users: response.entity._embedded.users});
		});
	}

	render() {
		return (
			<UserList users={this.state.users}/>
		)
	}
}
// end::app[]

// tag::users-list[]
class UserList extends React.Component{
	render() {
		var users = this.props.users.map(user =>
			<User key={users._links.self.href} user={user}/>
		);
		return (
			<table>
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
				{users}
			</table>
		)
	}
}
// end::user-list[]

// tag::user[]
class User extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.user.firstName}</td>
				<td>{this.props.user.lastName}</td>
				<td>{this.props.user.email}</td>
			</tr>
		)
	}
}
// end::user[]

// tag::render[]
React.render(
	<App />,
	document.getElementById('react')
)
// end::render[]
