export interface UserProfileDTO {
  username: string;
  email: string;
  abonnements: number[];
}

export interface UserProfileUpdateDTO {
  username?: string;
  email?: string;
  password?: string;
  desabonnements?: number[];
}