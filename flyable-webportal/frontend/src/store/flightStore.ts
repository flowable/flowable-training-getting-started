import { create, StateCreator } from 'zustand';
import { devtools } from 'zustand/middleware';

// Simple zustand store and types for the flight booking wizard as an alternative to using state.
type FlightDetails = {
  origin: string;
  destination: string;
  departureDate: Date | null;
  returnDate: Date | null;
  travelClass: string;
};

type PassengerInfo = {
  firstName: string;
  lastName: string;
  dateOfBirth: Date | null;
  gender: string;
  passportNumber: string;
  nationality: string;
};

type BillingAddress = {
  address: string;
  city: string;
  state: string;
  zip: string;
};

type FlightState = {
  flightDetails: FlightDetails;
  passengerInfo: PassengerInfo;
  selectedFlight: number | null;
  billingAddress: BillingAddress;
  optionalServices: Record<string, any>;
  totalPrice: string;
  bookingReference: string;
  setFlightDetails: (details: FlightDetails) => void;
  setPassengerInfo: (info: PassengerInfo) => void;
  setSelectedFlight: (flight: number | null) => void;
  setBillingAddress: (address: BillingAddress) => void;
  setOptionalServices: (services: Record<string, any>) => void;
  setTotalPrice: (price: string) => void;
  setBookingReference: (reference: string) => void;
  initializeDummyData: () => void;
};

const flightStore: StateCreator<FlightState> = set => ({
  flightDetails: {
    origin: '',
    destination: '',
    departureDate: null,
    returnDate: null,
    travelClass: '',
  },
  passengerInfo: {
    firstName: '',
    lastName: '',
    dateOfBirth: null,
    gender: '',
    passportNumber: '',
    nationality: '',
  },
  selectedFlight: null,
  billingAddress: {
    address: '',
    city: '',
    state: '',
    zip: '',
  },
  optionalServices: {},
  totalPrice: '',
  bookingReference: '',
  setFlightDetails: (details: FlightDetails) => set({ flightDetails: details }),
  setPassengerInfo: (info: PassengerInfo) => set({ passengerInfo: info }),
  setSelectedFlight: (flight: number | null) => set({ selectedFlight: flight }),
  setBillingAddress: (address: BillingAddress) => set({ billingAddress: address }),
  setOptionalServices: (services: Record<string, any>) => set({ optionalServices: services }),
  setTotalPrice: (price: string) => set({ totalPrice: price }),
  setBookingReference: (reference: string) => set({ bookingReference: reference }),
  initializeDummyData: () =>
    set({
      flightDetails: {
        origin: 'ZRH',
        destination: 'MAD',
        departureDate: new Date(),
        returnDate: new Date(),
        travelClass: 'economy',
      },
      passengerInfo: {
        firstName: 'John',
        lastName: 'Doe',
        dateOfBirth: new Date('1990-01-01'),
        gender: 'male',
        passportNumber: '123456789',
        nationality: 'US',
      },
      selectedFlight: 1,
      billingAddress: {
        address: '123 Main St',
        city: 'New York',
        state: 'NY',
        zip: '10001',
      },
      optionalServices: {},
      totalPrice: '500',
      bookingReference: 'ABC123',
    }),
});

export const useFlightStore = create<FlightState>()(devtools(flightStore));
