/** Simple "error" class for Flyable API errors if there is no actual error */
export class FlyableApiError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'FlyableApiError';
  }
}
