import { useEffect, useState } from "react";
import type { Appointment, AppointmentStatus } from "../types/appointment";
import { deleteAppointment, getAppointments, updateStatus } from "../api/appointments";

const STATUS_OPTIONS: AppointmentStatus[] = [
  "SCHEDULED",
  "COMPLETED",
  "CANCELLED",
];

interface AppointmentListProps {
  refreshKey: number;
}

export function AppointmentList({ refreshKey }: AppointmentListProps) {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);
    getAppointments()
      .then((data) => {
        if (!cancelled) setAppointments(data);
      })
      .catch((err: Error) => {
        if (!cancelled) setError(err.message);
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
  }, [refreshKey]);

  async function handleStatusChange(id: number, status: AppointmentStatus) {
    try {
      const updated = await updateStatus(id, status);
      setAppointments((prev) =>
        prev.map((appointment) => (appointment.id === id ? updated : appointment)),
      );
    } catch (err) {
      setError((err as Error).message);
    }
  }

  async function handleDelete(id: number) {
    try {
      await deleteAppointment(id);
      setAppointments((prev) => prev.filter((appointment) => appointment.id !== id));
    } catch (err) {
      setError((err as Error).message);
    }
  }

  if (loading) return <p>Loading appointments...</p>;
  if (error) return <p role="alert">Error: {error}</p>;
  if (appointments.length === 0) return <p>No appointments yet.</p>;

  return (
    <table>
      <thead>
        <tr>
          <th>Patient</th>
          <th>Doctor</th>
          <th>Date</th>
          <th>Status</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {appointments.map((appointment) => (
          <tr key={appointment.id}>
            <td>{appointment.patientName}</td>
            <td>{appointment.doctorName}</td>
            <td>{new Date(appointment.appointmentDate).toLocaleString()}</td>
            <td>
              <select
                aria-label={`Status for ${appointment.patientName}`}
                value={appointment.status}
                onChange={(e) =>
                  handleStatusChange(appointment.id, e.target.value as AppointmentStatus)
                }
              >
                {STATUS_OPTIONS.map((status) => (
                  <option key={status} value={status}>
                    {status}
                  </option>
                ))}
              </select>
            </td>
            <td>
              <button onClick={() => handleDelete(appointment.id)}>Delete</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
