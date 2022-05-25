package ro.ubb.opera.client.service;

import ro.ubb.opera.client.tcp.TcpClient;
import ro.ubb.opera.common.domain.Orchestra;
import ro.ubb.opera.common.service.OrchestraService;
import ro.ubb.opera.common.service.Service;
import ro.ubb.opera.common.service.exceptions.ServiceException;
import ro.ubb.opera.common.tcp.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class OrchestraServiceClient implements OrchestraService {
    private final ExecutorService executorService;

    private final TcpClient tcpClient;

    public OrchestraServiceClient(ExecutorService executorService, TcpClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Orchestra> addOrchestra(String name, String conductor) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.ADD_ORCHESTRA, name + Service.TOKENS_DELIMITER + conductor);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OrchestraService.decodeOrchestra(response.getBody());
        });
    }

    @Override
    public Future<Orchestra> findOrchestraById(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.FIND_ORCHESTRA_BY_ID, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OrchestraService.decodeOrchestra(response.getBody());
        });
    }

    @Override
    public Future<Orchestra> findOrchestraByName(String name) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.FIND_ORCHESTRA_BY_NAME, name);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OrchestraService.decodeOrchestra(response.getBody());
        });
    }

    @Override
    public Future<Set<Orchestra>> findOrchestrasByConductor(String conductor) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.FIND_ORCHESTRAS_BY_CONDUCTOR, conductor);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeOrchestraSet(response.getBody());
        });
    }

    @Override
    public Future<Set<Orchestra>> getAllOrchestras() {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.GET_ALL_ORCHESTRAS, "");

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return this.decodeOrchestraSet(response.getBody());
        });
    }

    @Override
    public Future<Orchestra> updateOrchestra(Integer id, String name, String conductor) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.UPDATE_ORCHESTRA, id.toString() + Service.TOKENS_DELIMITER + name + Service.TOKENS_DELIMITER + conductor);

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OrchestraService.decodeOrchestra(response.getBody());
        });
    }

    @Override
    public Future<Orchestra> deleteOrchestra(Integer id) {
        return this.executorService.submit(() -> {
            Message request = new Message(OrchestraService.DELETE_ORCHESTRA, id.toString());

            Message response = this.tcpClient.sendAndReceive(request);

            if (Message.ERROR.equals(response.getHeader())) {
                throw new ServiceException(response.getBody());
            }

            return OrchestraService.decodeOrchestra(response.getBody());
        });
    }

    private Set<Orchestra> decodeOrchestraSet(String encodedOrchestraSet) {
        Set<String> tokens = new HashSet<>();
        StringTokenizer stringTokenizer = new StringTokenizer(encodedOrchestraSet, Service.ENTITIES_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            tokens.add(stringTokenizer.nextToken());
        }
        return tokens.stream().map(OrchestraService::decodeOrchestra).collect(Collectors.toSet());
    }
}
