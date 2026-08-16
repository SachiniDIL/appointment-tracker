import { useState } from "react";
import { AppointmentForm } from "./components/AppointmentForm";
import { AppointmentList } from "./components/AppointmentList";
import "./App.css";

function App() {
  const [refreshKey, setRefreshKey] = useState(0);

  return (
    <>
      <h1>Appointment Tracker</h1>
      <AppointmentForm onCreated={() => setRefreshKey((key) => key + 1)} />
      <AppointmentList refreshKey={refreshKey} />
    </>
  );
}

export default App;
