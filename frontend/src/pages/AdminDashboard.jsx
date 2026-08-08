import { useEffect, useState } from "react";
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, CartesianGrid } from "recharts";
import api from "../services/api";

const SENTIMENT_COLORS = ["#22d3ee", "#f43f5e", "#818cf8"];

const AdminDashboard = () => {
  const [data, setData] = useState({
    subjectRatings: [],
    topFaculty: [],
    lowFaculty: [],
    totalFeedback: 0,
    totalCourses: 0,
    totalFaculty: 0,
    sentimentCount: {}
  });

  useEffect(() => {
    api.get("/admin/dashboard")
      .then((res) => setData(res.data))
      .catch(() => setData({
        subjectRatings: [],
        topFaculty: [],
        lowFaculty: [],
        totalFeedback: 0,
        totalCourses: 0,
        totalFaculty: 0,
        sentimentCount: {}
      }));
  }, []);

  const sentimentData = Object.entries(data.sentimentCount || {}).map(([name, value]) => ({ name, value }));
  const highestRatedSubject = [...data.subjectRatings].sort((a, b) => b.averageRating - a.averageRating)[0];
  const lowestRatedSubject = [...data.subjectRatings].sort((a, b) => a.averageRating - b.averageRating)[0];

  return (
    <div className="container">
      <div className="dashboard-header">
        <h2 className="page-title">Admin Dashboard</h2>
        <p className="page-subtitle">Track platform-wide quality metrics and identify both high-performing and at-risk areas.</p>
      </div>

      <div className="grid stats-grid">
        <div className="card metric-card">
          <h3 className="metric-title">Total Feedback</h3>
          <p className="metric-value">{data.totalFeedback || 0}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Subjects Tracked</h3>
          <p className="metric-value">{data.totalCourses || data.subjectRatings.length}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Faculty Members</h3>
          <p className="metric-value">{data.totalFaculty || 0}</p>
        </div>
      </div>

      <div className="grid">
        <div className="card chart">
          <h3>Average Ratings by Subject</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={data.subjectRatings}>
              <CartesianGrid strokeDasharray="3 3" stroke="#273c71" />
              <XAxis dataKey="subjectCode" stroke="#94a3b8" />
              <YAxis domain={[0, 5]} stroke="#94a3b8" />
              <Tooltip />
              <Bar dataKey="averageRating" fill="#6366f1" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
        <div className="card chart">
          <h3>Sentiment Overview</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie data={sentimentData} dataKey="value" nameKey="name" outerRadius={92}>
                {sentimentData.map((entry, index) => (
                  <Cell key={entry.name} fill={SENTIMENT_COLORS[index % SENTIMENT_COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="grid">
        <div className="card">
          <h3>Top Performing Faculty</h3>
          <ul className="list-clean">
            {data.topFaculty.map((f) => (
              <li key={f.facultyId}>
                <strong>{f.facultyName}</strong> - {f.averageRating.toFixed(2)}
                <div className="mini-meta">{f.feedbackCount || 0} feedback entries across {f.courseCount || 0} subjects</div>
              </li>
            ))}
          </ul>
        </div>
        <div className="card">
          <h3>Low Performing Faculty</h3>
          <ul className="list-clean">
            {data.lowFaculty.map((f) => (
              <li key={f.facultyId}>
                <strong>{f.facultyName}</strong> - {f.averageRating.toFixed(2)}
                <div className="mini-meta">{f.feedbackCount || 0} feedback entries across {f.courseCount || 0} subjects</div>
              </li>
            ))}
          </ul>
        </div>
      </div>

      <div className="card">
        <h3>Subject Quality Details</h3>
        <div className="table-wrap">
          <table className="analytics-table">
            <thead>
              <tr>
                <th>Subject</th>
                <th>Average Rating</th>
                <th>Total Feedback</th>
              </tr>
            </thead>
            <tbody>
              {data.subjectRatings.map((subject) => (
                <tr key={subject.subjectCode}>
                  <td>{subject.subjectCode} - {subject.subjectName}</td>
                  <td>{Number(subject.averageRating || 0).toFixed(2)}</td>
                  <td>{subject.feedbackCount || 0}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="insight-row">
          <span>Best Subject: {highestRatedSubject ? `${highestRatedSubject.subjectCode} (${highestRatedSubject.averageRating.toFixed(2)})` : "N/A"}</span>
          <span>Needs Attention: {lowestRatedSubject ? `${lowestRatedSubject.subjectCode} (${lowestRatedSubject.averageRating.toFixed(2)})` : "N/A"}</span>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
