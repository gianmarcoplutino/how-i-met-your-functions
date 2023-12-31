import React, { useState } from "react";
import "./App.css";

interface User {
  name: string;
  surname: string;
  email: string;
  birthdate: string;
  fileId: string;
}

interface FormData {
  name: string;
  surname: string;
  email: string;
  birthdate: string;
}

const App: React.FC = () => {
  const [formData, setFormData] = useState<FormData>({
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
    fetch("/persist", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    })
      .then(() => {
        window.alert("Richiesta presa in carico");
        setFormData({
          name: "",
          surname: "",
          email: "",
          birthdate: "",
        });
      })
      .catch((error) => console.error(error));
  };

  const handleGetUsers = () => {
    // change api URL
    fetch("/persist", {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => setUsers(data))
      .catch((error) => console.error(error));
  };

  const dummyUsers: User[] = [
    {
      name: "John",
      surname: "Doe",
      email: "john@example.com",
      birthdate: "1990-01-01",
      fileId: "1",
    },
    {
      name: "Jane",
      surname: "Doe",
      email: "jane@example.com",
      birthdate: "1995-05-15",
      fileId: "2",
    },
  ];
  const downloadPdf = (fileId: string) => {
  window.open("https://xmas-functionjava-test.azurewebsites.net/api/downloadPdfFunction?fileId=" + fileId, "_blank")
  }

  return (
    <div className="app">
      <form className="form" onSubmit={handleSubmit}>
        <label>
          <p>Nome:</p>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
          />
        </label>
        <label>
          <p>Cognome:</p>
          <input
            type="text"
            name="surname"
            value={formData.surname}
            onChange={handleChange}
          />
        </label>
        <label>
          <p>Mail:</p>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
          />
        </label>
        <label>
          <p>Data di nascita:</p>
          <input
            type="date"
            name="birthdate"
            value={formData.birthdate}
            onChange={handleChange}
          />
        </label>
        <button type="submit">Invia Utente</button>
      </form>
      <button type="button" onClick={handleGetUsers}>
        Ottieni Utenti
      </button>

      {users.length > 0 && (
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Cognome</th>
              <th>Mail</th>
              <th>Data di nascita</th>
              <th>Pdf</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr
                key={`${user.name}_${user.surname}_${user.email}_${user.birthdate}`}
              >
                <td>{user.name}</td>
                <td>{user.surname}</td>
                <td>{user.email}</td>
                <td>{user.birthdate}</td>
                <td>
                  <button type="button" onClick={() => downloadPdf(user.fileId)}>
                    Scarica
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default App;
