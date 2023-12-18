import React, { useState } from "react";
import "./App.css";

interface User {
  id: string;
  name: string;
  surname: string;
  email: string;
  birthdate: string;
}

const App: React.FC = () => {
  const [formData, setFormData] = useState<User>({
    id: "",
    name: "",
    surname: "",
    email: "",
    birthdate: "",
  });
  const [users, setUsers] = useState<Array<User>>([]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // change api URL
    fetch("https://example.com/api", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then(() => window.alert("Richiesta presa in carico"))
      .catch((error) => console.error(error));
  };

  const handleGetUsers = () => {
    // change api URL
    fetch("https://example.com/api")
      .then((response) => response.json())
      .then((data) => setUsers(data))
      .catch((error) => console.error(error));
  };

  return (
    <div className="app">
      <form className="form" onSubmit={handleSubmit}>
        <label>
          Name:
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
        </label>
        <label>
          Surname:
          <input
            type="text"
            name="surname"
            value={formData.surname}
            onChange={handleChange}
          />
        </label>
        <label>
          Email:
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
        </label>
        <label>
          Birthdate:
          <input
            type="date"
            name="birthdate"
            value={formData.birthdate}
            onChange={handleChange}
          />
        </label>
        <button type="submit">Submit</button>
      </form>
      <button onClick={handleGetUsers}>Get Users</button>

      {users.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Surname</th>
              <th>Email</th>
              <th>Birthdate</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.surname}</td>
                <td>{user.email}</td>
                <td>{user.birthdate}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default App;
