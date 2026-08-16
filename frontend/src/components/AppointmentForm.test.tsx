import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { AppointmentForm } from "./AppointmentForm";

describe("AppointmentForm", () => {
  beforeEach(() => {
    vi.stubGlobal("fetch", vi.fn());
  });

  afterEach(() => {
    vi.unstubAllGlobals();
  });

  it("submits the form and calls onCreated on success", async () => {
    const user = userEvent.setup();
    const onCreated = vi.fn();
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => ({
        id: 1,
        patientName: "Jane Doe",
        doctorName: "Dr. Smith",
        appointmentDate: "2026-09-01T10:00:00",
        status: "SCHEDULED",
        createdAt: "2026-08-01T10:00:00",
      }),
    });

    render(<AppointmentForm onCreated={onCreated} />);

    await user.type(screen.getByLabelText("Patient name"), "Jane Doe");
    await user.type(screen.getByLabelText("Doctor name"), "Dr. Smith");
    const dateInput = screen.getByLabelText("Appointment date");
    await user.type(dateInput, "2026-09-01T10:00");
    await user.click(screen.getByRole("button", { name: "Book appointment" }));

    await waitFor(() => expect(onCreated).toHaveBeenCalledTimes(1));

    expect(fetch).toHaveBeenCalledWith(
      "http://localhost:8080/api/appointments",
      expect.objectContaining({
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          patientName: "Jane Doe",
          doctorName: "Dr. Smith",
          appointmentDate: new Date("2026-09-01T10:00").toISOString(),
        }),
      }),
    );
  });

  it("shows an error and does not call onCreated when the API rejects the request", async () => {
    const user = userEvent.setup();
    const onCreated = vi.fn();
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      statusText: "Bad Request",
      json: async () => ({ message: "appointmentDate must be in the future" }),
    });

    render(<AppointmentForm onCreated={onCreated} />);

    await user.type(screen.getByLabelText("Patient name"), "Jane Doe");
    await user.type(screen.getByLabelText("Doctor name"), "Dr. Smith");
    await user.type(
      screen.getByLabelText("Appointment date"),
      "2020-01-01T10:00",
    );
    await user.click(screen.getByRole("button", { name: "Book appointment" }));

    expect(await screen.findByRole("alert")).toHaveTextContent(
      "appointmentDate must be in the future",
    );
    expect(onCreated).not.toHaveBeenCalled();
  });
});
