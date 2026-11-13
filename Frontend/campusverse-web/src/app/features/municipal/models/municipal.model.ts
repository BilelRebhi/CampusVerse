export type RequestStatus = 'PENDING' | 'IN_REVIEW' | 'APPROVED' | 'REJECTED' | 'COMPLETED';

export interface MunicipalService {
  id: string;
  name: string;
  description: string;
  processingDelayDays: number;
}

export interface ApplicationRequest {
  id: string;
  citizenId: string;
  service: MunicipalService;
  status: RequestStatus;
}

export interface CreateApplicationDto {
  citizenId?: string;
  serviceId: string;
}

export interface UpdateStatusDto {
  status: RequestStatus;
}
