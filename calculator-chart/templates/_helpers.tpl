
{{- define "calculator-chart.selectorLabels" -}}
app.kubernetes.io/name: {{  .Chart.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Chart name and version as used by the chart label
*/}}
{{- define "calculator-chart.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "calculator-chart.labels" -}}
helm.sh/chart: {{ include "calculator-chart.chart" . }}
{{ include "calculator-chart.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{- define "calculator-chart.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
    {{ default (printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-") .Values.serviceAccount.name }}
{{- else -}}
    {{ default "default" .Values.serviceAccount.name }}
{{- end -}}
{{- end -}}

{{/*
Names of userapi tier components
*/}}
{{- define "calculator-chart.userapi.defaultName" -}}
{{- printf "userapi-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.userapi.deployment.name" -}}
{{- default (include "calculator-chart.userapi.defaultName" .) .Values.userapi.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.userapi.container.name" -}}
{{- default (include "calculator-chart.userapi.defaultName" .) .Values.userapi.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.userapi.service.name" -}}
{{- default (include "calculator-chart.userapi.defaultName" .) .Values.userapi.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.userapi.hpa.name" -}}
{{- default (include "calculator-chart.userapi.defaultName" .) .Values.userapi.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Names of cardapi tier components
*/}}
{{- define "calculator-chart.cardapi.defaultName" -}}
{{- printf "cardapi-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.cardapi.deployment.name" -}}
{{- default (include "calculator-chart.cardapi.defaultName" .) .Values.cardapi.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.cardapi.container.name" -}}
{{- default (include "calculator-chart.cardapi.defaultName" .) .Values.cardapi.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.cardapi.service.name" -}}
{{- default (include "calculator-chart.cardapi.defaultName" .) .Values.cardapi.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.cardapi.hpa.name" -}}
{{- default (include "calculator-chart.cardapi.defaultName" .) .Values.cardapi.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Names of gateway tier components
*/}}
{{- define "calculator-chart.gateway.defaultName" -}}
{{- printf "gateway-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.gateway.deployment.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.deployment.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.container.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.container.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.service.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.service.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "calculator-chart.gateway.hpa.name" -}}
{{- default (include "calculator-chart.gateway.defaultName" .) .Values.gateway.hpa.name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Annotation to update pods on Secrets or ConfigMaps updates
*/}}
{{- define "calculator-chart.propertiesHash" -}}
{{- $secrets := include (print $.Template.BasePath "/secrets.yaml") . | sha256sum -}}
{{- $urlConfig := include (print $.Template.BasePath "/url-config.yaml") . | sha256sum -}}
{{ print $secrets $urlConfig | sha256sum }}
{{- end -}}

{{/*
Names of other components
*/}}
{{- define "calculator-chart.secrets.defaultName" -}}
{{- printf "secrets-%s" .Release.Name -}}
{{- end -}}

{{- define "calculator-chart.urlConfig.defaultName" -}}
{{- printf "url-config-%s" .Release.Name -}}
{{- end -}}

