import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../context/AuthContext";

const LoginPage = () => {
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const { data } = await api.post("/auth/login", form);
      login(data);
      navigate(data.role === "STUDENT" ? "/student" : data.role === "FACULTY" ? "/faculty" : "/admin");
    } catch (err) {
      setError(err.response?.data?.error || "Login failed");
    }
  };

  return (
    <div className="container auth-shell">
      <h2 className="page-title">Welcome back</h2>
      <p className="page-subtitle">Login to continue to your dashboard.</p>
      <form onSubmit={handleSubmit} className="card form auth-form form-dark">
        <input placeholder="Email" type="email" required onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input placeholder="Password" type="password" required onChange={(e) => setForm({ ...form, password: e.target.value })} />
        <button type="submit">Login</button>
        {error && <p className="error">{error}</p>}
      </form>
      <p className="auth-link">New user? <Link to="/register">Register</Link></p>
    </div>
  );
};

export default LoginPage;
