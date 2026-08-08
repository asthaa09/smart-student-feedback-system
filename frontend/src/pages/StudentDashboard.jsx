import { useEffect, useState } from "react";
import api from "../services/api";

const StudentDashboard = () => {
  const [courses, setCourses] = useState([]);
  const [facultyDirectory, setFacultyDirectory] = useState([]);
  const [message, setMessage] = useState("");
  const [form, setForm] = useState({
    courseId: "",
    courseRating: 5,
    courseComment: "",
    facultyRating: 5,
    facultyComment: "",
    anonymous: false
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([api.get("/student/courses"), api.get("/student/faculty-directory")])
      .then(([coursesRes, facultyRes]) => {
        setCourses(coursesRes.data || []);
        setFacultyDirectory(facultyRes.data || []);
      })
      .catch(() => {
        setCourses([]);
        setFacultyDirectory([]);
      })
      .finally(() => setLoading(false));
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/student/feedback", {
        ...form,
        courseId: Number(form.courseId),
        courseRating: Number(form.courseRating),
        facultyRating: Number(form.facultyRating)
      });
      setMessage("Feedback submitted successfully.");
      setForm({
        courseId: "",
        courseRating: 5,
        courseComment: "",
        facultyRating: 5,
        facultyComment: "",
        anonymous: false
      });
    } catch {
      setMessage("Could not submit feedback.");
    }
  };

  const fallbackFacultyFromCourses = Array.from(
    new Map(
      courses
        .filter((course) => course.faculty)
        .map((course) => [course.faculty.id, { facultyId: course.faculty.id, facultyName: course.faculty.fullName, subjects: [] }])
    ).values()
  ).map((faculty) => ({
    ...faculty,
    subjects: courses.filter((course) => course.faculty?.id === faculty.facultyId).map((course) => course.subjectCode)
  }));
  const facultyList = facultyDirectory.length ? facultyDirectory : fallbackFacultyFromCourses;

  const feedbackTips = [
    "Mention one thing that worked well in the class.",
    "Add one specific improvement suggestion.",
    "Use respectful language and concrete examples."
  ];
  const selectedCourse = courses.find((course) => String(course.id) === String(form.courseId));
  const selectedFacultyName = selectedCourse?.faculty?.fullName || "Not selected";

  return (
    <div className="container">
      <div className="dashboard-header">
        <h2 className="page-title">Student Dashboard</h2>
        <p className="page-subtitle">Share actionable feedback with clear context for each faculty and subject.</p>
      </div>

      <div className="grid stats-grid">
        <div className="card metric-card">
          <h3 className="metric-title">Available Subjects</h3>
          <p className="metric-value">{courses.length}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Faculty Members</h3>
          <p className="metric-value">{facultyList.length}</p>
        </div>
        <div className="card metric-card">
          <h3 className="metric-title">Feedback Score Selected</h3>
          <p className="metric-value">{form.courseRating}/5</p>
        </div>
      </div>

      <form className="card form form-dark" onSubmit={submit}>
        <h3>Submit Feedback</h3>
        <p className="muted-text">Selected Faculty: {selectedFacultyName}</p>
        <select required value={form.courseId} onChange={(e) => setForm({ ...form, courseId: e.target.value })}>
          <option value="">Select course</option>
          {courses.map((c) => (
            <option key={c.id} value={c.id}>{c.subjectCode} - {c.subjectName}</option>
          ))}
        </select>
        <div className="split-inputs">
          <div>
            <label className="field-label">Course Rating (1-5)</label>
            <input
              type="number"
              min="1"
              max="5"
              value={form.courseRating}
              onChange={(e) => setForm({ ...form, courseRating: e.target.value })}
            />
          </div>
          <div>
            <label className="field-label">Faculty Rating (1-5)</label>
            <input
              type="number"
              min="1"
              max="5"
              value={form.facultyRating}
              onChange={(e) => setForm({ ...form, facultyRating: e.target.value })}
            />
          </div>
        </div>
        <textarea
          placeholder="Course feedback (syllabus, pace, assignments, materials)..."
          required
          value={form.courseComment}
          onChange={(e) => setForm({ ...form, courseComment: e.target.value })}
        />
        <textarea
          placeholder="Faculty feedback (teaching clarity, support, communication)..."
          required
          value={form.facultyComment}
          onChange={(e) => setForm({ ...form, facultyComment: e.target.value })}
        />
        <label className="checkbox-row">
          <input type="checkbox" checked={form.anonymous} onChange={(e) => setForm({ ...form, anonymous: e.target.checked })} />
          Submit anonymously
        </label>
        <button type="submit">Submit Feedback</button>
      </form>
      {message && <p className="status-message">{message}</p>}

      <div className="grid">
        <div className="card">
          <h3>Faculty Directory</h3>
          {loading ? (
            <p className="muted-text">Loading faculty information...</p>
          ) : facultyList.length === 0 ? (
            <p className="muted-text">No faculty assignments found yet.</p>
          ) : (
            <ul className="list-clean">
              {facultyList.map((faculty) => (
                <li key={faculty.facultyId}>
                  <strong>{faculty.facultyName}</strong>
                  <div className="mini-meta">
                    Teaches: {faculty.subjects.length ? faculty.subjects.join(", ") : "No subjects assigned yet"}
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
        <div className="card">
          <h3>How To Write Useful Feedback</h3>
          <ul className="list-clean">
            {feedbackTips.map((tip) => (
              <li key={tip}>{tip}</li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default StudentDashboard;
