package service;

import entity.*;
import exception.*;
import repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AppointmentService {

    private DoctorRepository doctorRepo = new DoctorRepository();
    private AppointmentRepository appointmentRepo =
            new AppointmentRepository();

    public void bookAppointment(int patientId, int doctorId,
                                LocalDate date, LocalTime time) {

        Doctor doctor = doctorRepo.findById(doctorId);
        if (doctor == null ||
                time.isBefore(doctor.getAvailableFrom()) ||
                time.isAfter(doctor.getAvailableTo())) {
            throw new DoctorUnavailableException();
        }

        if (appointmentRepo.exists(doctorId, date, time)) {
            throw new TimeSlotAlreadyBookedException();
        }

        appointmentRepo.save(
                new Appointment(patientId, doctorId, date, time, "BOOKED")
        );
    }

    public void cancelAppointment(int appointmentId) {
        appointmentRepo.updateStatus(appointmentId, "CANCELLED");
    }

    public List<Appointment> getMyAppointments(int patientId) {
        return appointmentRepo.findByPatient(patientId);
    }

    public List<Appointment> getDoctorSchedule(int doctorId) {
        return appointmentRepo.findByDoctor(doctorId);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }
}
