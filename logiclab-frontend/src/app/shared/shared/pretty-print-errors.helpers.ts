export function prettyPrintError(error: number): string {
    switch (error) {
        case 404:
            return 'Game not found. Please check the game ID.';
        case 500:
            return 'Internal server error. Please try again later.';
        case 400:
            return 'Bad request. Please check your input.';
        default:
            return 'An unexpected error occurred. Please try again.';
    }
}