import type {
  Appointment,
  AppointmentRequest,
  AppointmentStatus,
  ApiErrorBody,
} from "../types/appointment";

const BASE_URL = "http://localhost:8080/api/appointments";

async function parseErrorMessage(response: Response): Promise<string> {
  try {
    const body: ApiErrorBody = await response.json();
    return body.message ?? response.statusText;
  } catch {
    return response.statusText;
  }
}

export async function getAppointments(): Promise<Appointment[]> {
  const response = await fetch(BASE_URL);
  if (!response.ok) {
    throw new Error(await parseErrorMessage(response));
  }
  return response.json();
}

export async function createAppointment(
  request: AppointmentRequest,
): Promise<Appointment> {
  const response = await fetch(BASE_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
  });
  if (!response.ok) {
    throw new Error(await parseErrorMessage(response));
  }
  return response.json();
}

export async function updateStatus(
  id: number,
  status: AppointmentStatus,
): Promise<Appointment> {
  const response = await fetch(`${BASE_URL}/${id}/status`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(status),
  });
  if (!response.ok) {
    throw new Error(await parseErrorMessage(response));
  }
  return response.json();
}

export async function deleteAppointment(id: number): Promise<void> {
  const response = await fetch(`${BASE_URL}/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    throw new Error(await parseErrorMessage(response));
  }
}
