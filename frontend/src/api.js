import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
})

export function getDepartments() {
  return api.get('/api/departments')
}

export function createDepartment(department) {
  return api.post('/api/departments', department)
}

export function getEmployees() {
  return api.get('/api/employees')
}

export function createEmployee(employee) {
  return api.post('/api/employees', employee)
}
