import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const NavBar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const onLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav className="nav">
      <Link to="/" className="brand">Smart Feedback</Link>
      <div className="nav-actions">
        {!user && <Link to="/register">Register</Link>}
        {!user && <Link to="/login">Login</Link>}
        {user?.role === "STUDENT" && <Link to="/student">Student Dashboard</Link>}
        {user?.role === "FACULTY" && <Link to="/faculty">Faculty Dashboard</Link>}
        {user?.role === "ADMIN" && <Link to="/admin">Admin Dashboard</Link>}
        {user && <button className="btn-secondary" onClick={onLogout}>Logout</button>}
      </div>
    </nav>
  );
};

export default NavBar;
