import { render, screen, waitFor, within } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { AppointmentList } from "./AppointmentList";
import type { Appointment } from "../types/appointment";

const appointment: Appointment = {
  id: 1,
  patientName: "Jane Doe",
  doctorName: "Dr. Smith",
  appointmentDate: "2026-09-01T10:00:00",
  status: "SCHEDULED",
  createdAt: "2026-08-01T10:00:00",
};

describe("AppointmentList", () => {
  beforeEach(() => {
    vi.stubGlobal("fetch", vi.fn());
  });

  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("renders appointments returned from the API", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => [appointment],
    });

    render(<AppointmentList refreshKey={0} />);

    expect(await screen.findByText("Jane Doe")).toBeInTheDocument();
    expect(screen.getByText("Dr. Smith")).toBeInTheDocument();
  });

  it("shows an error message when the fetch fails", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      statusText: "Internal Server Error",
      json: async () => ({ message: "Something went wrong" }),
    });

    render(<AppointmentList refreshKey={0} />);

    expect(await screen.findByRole("alert")).toHaveTextContent("Something went wrong");
  });

  it("calls the delete endpoint and removes the row on click", async () => {
    const user = userEvent.setup();
    (fetch as ReturnType<typeof vi.fn>)
      .mockResolvedValueOnce({ ok: true, json: async () => [appointment] })
      .mockResolvedValueOnce({ ok: true, json: async () => ({}) });

    render(<AppointmentList refreshKey={0} />);
    await screen.findByText("Jane Doe");

    await user.click(screen.getByRole("button", { name: "Delete" }));

    await waitFor(() => expect(screen.queryByText("Jane Doe")).not.toBeInTheDocument());
    expect(fetch).toHaveBeenLastCalledWith(
      "http://localhost:8080/api/appointments/1",
      expect.objectContaining({ method: "DELETE" }),
    );
  });

  it("calls the status endpoint when a new status is selected", async () => {
    const user = userEvent.setup();
    const updatedAppointment = { ...appointment, status: "COMPLETED" as const };
    (fetch as ReturnType<typeof vi.fn>)
      .mockResolvedValueOnce({ ok: true, json: async () => [appointment] })
      .mockResolvedValueOnce({ ok: true, json: async () => updatedAppointment });

    render(<AppointmentList refreshKey={0} />);
    await screen.findByText("Jane Doe");

    const select = screen.getByLabelText("Status for Jane Doe");
    await user.selectOptions(select, "COMPLETED");

    await waitFor(() =>
      expect(fetch).toHaveBeenLastCalledWith(
        "http://localhost:8080/api/appointments/1/status",
        expect.objectContaining({ method: "PATCH", body: JSON.stringify("COMPLETED") }),
      ),
    );
    expect(within(select).getByRole("option", { name: "COMPLETED" }).selected).toBe(true);
  });
});
