import { useState } from "react";
import type { FormEvent } from "react";
import { createAppointment } from "../api/appointments";

interface AppointmentFormProps {
  onCreated: () => void;
}

export function AppointmentForm({ onCreated }: AppointmentFormProps) {
  const [patientName, setPatientName] = useState("");
  const [doctorName, setDoctorName] = useState("");
  const [appointmentDate, setAppointmentDate] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      await createAppointment({
        patientName,
        doctorName,
        appointmentDate: new Date(appointmentDate).toISOString(),
      });
      setPatientName("");
      setDoctorName("");
      setAppointmentDate("");
      onCreated();
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label htmlFor="patientName">Patient name</label>
        <input
          id="patientName"
          value={patientName}
          onChange={(e) => setPatientName(e.target.value)}
          required
        />
      </div>
      <div>
        <label htmlFor="doctorName">Doctor name</label>
        <input
          id="doctorName"
          value={doctorName}
          onChange={(e) => setDoctorName(e.target.value)}
          required
        />
      </div>
      <div>
        <label htmlFor="appointmentDate">Appointment date</label>
        <input
          id="appointmentDate"
          type="datetime-local"
          value={appointmentDate}
          onChange={(e) => setAppointmentDate(e.target.value)}
          required
        />
      </div>
      {error && <p role="alert">Error: {error}</p>}
      <button type="submit" disabled={submitting}>
        {submitting ? "Booking..." : "Book appointment"}
      </button>
    </form>
  );
}
