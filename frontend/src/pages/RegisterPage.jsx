import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

const RegisterPage = () => {
  const [form, setForm] = useState({ fullName: "", email: "", password: "", role: "STUDENT" });
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const {data} = await api.post("/auth/register", form);
      login(data);
      navigate(data.role === "STUDENT" ? "/student" : data.role === "FACULTY" ? "/faculty" : "/admin");
    } catch (err) {
      console.log("ERROR:", err.response);
      setError(
          err.response?.data?.message ||
          err.response?.data?.error ||
          JSON.stringify(err.response?.data) ||
          "Registration failed"
      );
    }
  };
  return (
    <div className="container auth-shell">
      <h2 className="page-title">Create account</h2>
      <p className="page-subtitle">Register to submit and view smart feedback insights.</p>
      <form onSubmit={handleSubmit} className="card form auth-form form-dark"><input
          placeholder="Full Name"
          required
          value={form.fullName}
          onChange={(e) => setForm({ ...form, fullName: e.target.value })}
      />

        <input
            placeholder="Email"
            type="email"
            required
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
        />

        <input
            placeholder="Password"
            type="password"
            required
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
        />
        <select onChange={(e) => setForm({ ...form, role: e.target.value })} value={form.role}>
          <option value="STUDENT">Student</option>
          <option value="FACULTY">Faculty</option>
          <option value="ADMIN">Admin</option>
        </select>
        <button type="submit">Register</button>
        {error && <p className="error">{error}</p>}
      </form>
      <p className="auth-link">Already have an account? <Link to="/login">Login</Link></p>
    </div>
  );
};

export default RegisterPage;
