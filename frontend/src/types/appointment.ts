export type AppointmentStatus = "SCHEDULED" | "COMPLETED" | "CANCELLED";

export interface Appointment {
  id: number;
  patientName: string;
  doctorName: string;
  appointmentDate: string;
  status: AppointmentStatus;
  createdAt: string;
}

export interface AppointmentRequest {
  patientName: string;
  doctorName: string;
  appointmentDate: string;
}

export interface ApiErrorBody {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  fields?: Record<string, string>;
}
