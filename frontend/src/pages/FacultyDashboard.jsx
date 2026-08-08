import { useEffect, useState } from "react";
import { Pie, PieChart, Cell, Tooltip, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid } from "recharts";
import api from "../services/api";

const COLORS = ["#22d3ee", "#f43f5e", "#818cf8"];

const FacultyDashboard = () => {
  const [data, setData] = useState(null);
  const [courses, setCourses] = useState([]);
  const [subjectFilter, setSubjectFilter] = useState("ALL");
  const [sentimentFilter, setSentimentFilter] = useState("ALL");

  useEffect(() => {
    Promise.all([api.get("/faculty/feedback"), api.get("/faculty/courses")])
      .then(([feedbackRes, coursesRes]) => {
        setData(feedbackRes.data);
        setCourses(coursesRes.data || []);
      })
      .catch(() => {
        setData(null);
        setCourses([]);
      });
  }, []);

  const chartData = data
    ? Object.entries(data.sentimentCount || {}).map(([name, value]) => ({ name, value }))
    : [];
  const ratingDistribution = data
    ? [5, 4, 3, 2, 1].map((rating) => ({
      rating: `${rating}★`,
      count: Number(data.ratingDistribution?.[rating] || 0)
    }))
    : [];
  const feedbackList = data?.feedback || [];
  const filteredFeedback = feedbackList.filter((item) => {
    const subjectMatch = subjectFilter === "ALL" || item.subjectCode === subjectFilter;
    const sentimentMatch = sentimentFilter === "ALL" || item.sentiment === sentimentFilter;
    return subjectMatch && sentimentMatch;
  });
  const subjectOptions = Array.from(new Set(feedbackList.map((item) => item.subjectCode)));

  return (
    <div className="container">
      <div className="dashboard-header">
        <h2 className="page-title">Faculty Dashboard</h2>
        <p className="page-subtitle">Understand subject-level performance and quickly identify where intervention is needed.</p>
      </div>
      <div className="grid stats-grid">
        <div className="card metric-card">
          <h3 className="metric-title">Average Rating</h3>
          <p className="metric-value">{data ? data.averageRating.toFixed(2) : "0.00"}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Feedback Entries</h3>
          <p className="metric-value">{data?.totalFeedback || feedbackList.length || 0}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Subjects Covered</h3>
          <p className="metric-value">{data?.totalCourses || courses.length || 0}</p>
        </div>
      </div>
      <div className="grid">
        <div className="card chart">
          <h3>Sentiment Distribution</h3>
          <ResponsiveContainer width="100%" height={280}>
            <PieChart>
              <Pie data={chartData} dataKey="value" nameKey="name" outerRadius={90}>
                {chartData.map((entry, index) => (
                  <Cell key={entry.name} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>
        <div className="card chart">
          <h3>Rating Distribution</h3>
          <ResponsiveContainer width="100%" height={280}>
            <BarChart data={ratingDistribution}>
              <CartesianGrid strokeDasharray="3 3" stroke="#273c71" />
              <XAxis dataKey="rating" stroke="#94a3b8" />
              <YAxis allowDecimals={false} stroke="#94a3b8" />
              <Tooltip />
              <Bar dataKey="count" fill="#22d3ee" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="card">
        <div className="section-row">
          <h3>Feedback List</h3>
          <div className="inline-filters">
            <select value={subjectFilter} onChange={(e) => setSubjectFilter(e.target.value)}>
              <option value="ALL">All Subjects</option>
              {subjectOptions.map((subjectCode) => (
                <option key={subjectCode} value={subjectCode}>{subjectCode}</option>
              ))}
            </select>
            <select value={sentimentFilter} onChange={(e) => setSentimentFilter(e.target.value)}>
              <option value="ALL">All Sentiments</option>
              <option value="POSITIVE">Positive</option>
              <option value="NEUTRAL">Neutral</option>
              <option value="NEGATIVE">Negative</option>
            </select>
          </div>
        </div>
        <ul className="list-clean">
          {filteredFeedback.map((item) => (
            <li key={item.id}>
              <strong>{item.subjectCode}</strong> ({item.facultyRating}/5, {item.sentiment}) - {item.facultyComment}
              <div className="mini-meta">Course rating: {item.courseRating}/5 | By {item.studentName}</div>
            </li>
          ))}
        </ul>
      </div>

      <div className="card">
        <h3>Subject Performance Summary</h3>
        <div className="table-wrap">
          <table className="analytics-table">
            <thead>
              <tr>
                <th>Subject</th>
                <th>Average Rating</th>
                <th>Feedback Count</th>
              </tr>
            </thead>
            <tbody>
              {(data?.courseStats || []).map((course) => (
                <tr key={course.subjectCode}>
                  <td>{course.subjectCode} - {course.subjectName}</td>
                  <td>{Number(course.averageRating || 0).toFixed(2)}</td>
                  <td>{course.feedbackCount || 0}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default FacultyDashboard;
