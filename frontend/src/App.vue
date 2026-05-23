<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  createDepartment,
  createEmployee,
  getDepartments,
  getEmployees,
} from './api'

const departments = ref([])
const employees = ref([])
const loading = ref(true)
const savingDepartment = ref(false)
const savingEmployee = ref(false)
const error = ref('')
const success = ref('')

const departmentForm = reactive({
  name: '',
  description: '',
})

const employeeForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
  jobTitle: '',
  hireDate: new Date().toISOString().slice(0, 10),
  departmentId: '',
})

const totalDepartments = computed(() => departments.value.length)
const totalEmployees = computed(() => employees.value.length)

const recentEmployees = computed(() =>
  [...employees.value]
    .sort((a, b) => String(b.createdAt).localeCompare(String(a.createdAt)))
    .slice(0, 5),
)

async function loadData() {
  loading.value = true
  error.value = ''

  try {
    const [departmentResponse, employeeResponse] = await Promise.all([
      getDepartments(),
      getEmployees(),
    ])

    departments.value = departmentResponse.data
    employees.value = employeeResponse.data

    if (!employeeForm.departmentId && departments.value.length > 0) {
      employeeForm.departmentId = departments.value[0].id
    }
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    loading.value = false
  }
}

async function submitDepartment() {
  savingDepartment.value = true
  error.value = ''
  success.value = ''

  try {
    await createDepartment({
      name: departmentForm.name,
      description: departmentForm.description,
    })

    departmentForm.name = ''
    departmentForm.description = ''
    success.value = 'Department created.'
    await loadData()
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    savingDepartment.value = false
  }
}

async function submitEmployee() {
  savingEmployee.value = true
  error.value = ''
  success.value = ''

  try {
    await createEmployee({
      firstName: employeeForm.firstName,
      lastName: employeeForm.lastName,
      email: employeeForm.email,
      jobTitle: employeeForm.jobTitle,
      hireDate: employeeForm.hireDate,
      departmentId: Number(employeeForm.departmentId),
    })

    employeeForm.firstName = ''
    employeeForm.lastName = ''
    employeeForm.email = ''
    employeeForm.jobTitle = ''
    success.value = 'Employee created.'
    await loadData()
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    savingEmployee.value = false
  }
}

function getErrorMessage(err) {
  return err?.response?.data?.message || err?.message || 'Something went wrong.'
}

onMounted(loadData)
</script>

<template>
  <main class="app-shell">
    <header class="topbar">
      <div>
        <p class="eyebrow">Employee Management System</p>
        <h1>People operations</h1>
      </div>
      <button class="ghost-button" type="button" @click="loadData">Refresh</button>
    </header>

    <section class="metrics" aria-label="Summary">
      <div class="metric">
        <span>Total employees</span>
        <strong>{{ totalEmployees }}</strong>
      </div>
      <div class="metric">
        <span>Departments</span>
        <strong>{{ totalDepartments }}</strong>
      </div>
      <div class="metric">
        <span>API status</span>
        <strong>{{ loading ? 'Loading' : 'Connected' }}</strong>
      </div>
    </section>

    <p v-if="error" class="alert error">{{ error }}</p>
    <p v-if="success" class="alert success">{{ success }}</p>

    <section class="workspace">
      <div class="panel">
        <div class="panel-heading">
          <h2>Create department</h2>
        </div>

        <form class="form-grid" @submit.prevent="submitDepartment">
          <label>
            Department name
            <input v-model.trim="departmentForm.name" required maxlength="100" placeholder="Engineering" />
          </label>

          <label>
            Description
            <textarea
              v-model.trim="departmentForm.description"
              maxlength="500"
              rows="4"
              placeholder="Builds and maintains software systems"
            />
          </label>

          <button class="primary-button" type="submit" :disabled="savingDepartment">
            {{ savingDepartment ? 'Saving...' : 'Create department' }}
          </button>
        </form>
      </div>

      <div class="panel">
        <div class="panel-heading">
          <h2>Create employee</h2>
        </div>

        <form class="form-grid two-column" @submit.prevent="submitEmployee">
          <label>
            First name
            <input v-model.trim="employeeForm.firstName" required maxlength="100" placeholder="Ava" />
          </label>

          <label>
            Last name
            <input v-model.trim="employeeForm.lastName" required maxlength="100" placeholder="Patel" />
          </label>

          <label>
            Email
            <input v-model.trim="employeeForm.email" required type="email" maxlength="255" placeholder="ava.patel@example.com" />
          </label>

          <label>
            Job title
            <input v-model.trim="employeeForm.jobTitle" required maxlength="150" placeholder="Software Engineer" />
          </label>

          <label>
            Hire date
            <input v-model="employeeForm.hireDate" required type="date" />
          </label>

          <label>
            Department
            <select v-model="employeeForm.departmentId" required>
              <option disabled value="">Select department</option>
              <option v-for="department in departments" :key="department.id" :value="department.id">
                {{ department.name }}
              </option>
            </select>
          </label>

          <button class="primary-button span-all" type="submit" :disabled="savingEmployee || departments.length === 0">
            {{ savingEmployee ? 'Saving...' : 'Create employee' }}
          </button>
        </form>
      </div>
    </section>

    <section class="data-section">
      <div class="table-panel">
        <div class="panel-heading">
          <h2>Employees</h2>
        </div>

        <div v-if="loading" class="empty-state">Loading employees...</div>
        <div v-else-if="employees.length === 0" class="empty-state">No employees yet.</div>
        <table v-else>
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>Title</th>
              <th>Department</th>
              <th>Hire date</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="employee in employees" :key="employee.id">
              <td>{{ employee.firstName }} {{ employee.lastName }}</td>
              <td>{{ employee.email }}</td>
              <td>{{ employee.jobTitle }}</td>
              <td>{{ employee.departmentName }}</td>
              <td>{{ employee.hireDate }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <aside class="side-list">
        <div class="panel-heading">
          <h2>Recent hires</h2>
        </div>
        <ul v-if="recentEmployees.length > 0">
          <li v-for="employee in recentEmployees" :key="employee.id">
            <strong>{{ employee.firstName }} {{ employee.lastName }}</strong>
            <span>{{ employee.departmentName }}</span>
          </li>
        </ul>
        <p v-else class="empty-state">No recent hires.</p>
      </aside>
    </section>
  </main>
</template>
